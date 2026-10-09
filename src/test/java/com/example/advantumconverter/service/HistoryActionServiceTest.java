package com.example.advantumconverter.service;

import com.example.advantumconverter.enums.HistoryActionType;
import com.example.advantumconverter.model.jpa.HistoryAction;
import com.example.advantumconverter.model.jpa.HistoryActionRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HistoryActionServiceTest {

    @Mock
    private HistoryActionRepository repository;

    private HistoryActionService service;

    private User user(long chatId) {
        var user = new User();
        user.setChatId(chatId);
        return user;
    }

    @BeforeEach
    void setUp() {
        service = new HistoryActionService(repository);
        ReflectionTestUtils.setField(service, "enabled", true);
    }

    @Test
    void saveHistoryAction_disabled_doesNothing() {
        ReflectionTestUtils.setField(service, "enabled", false);
        service.saveHistoryAction(user(1L), new Update());
        verify(repository, never()).save(any());
    }

    @Test
    void saveHistoryAction_messageText_saved() {
        var message = new Message();
        message.setText("hello");
        var update = new Update();
        update.setMessage(message);

        service.saveHistoryAction(user(1L), update);

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.getChatIdFrom()).isEqualTo(1L);
        assertThat(saved.getActionType()).isEqualTo(HistoryActionType.USER_ACTION);
        assertThat(saved.getMessageText()).isEqualTo("hello");
    }

    @Test
    void saveHistoryAction_messageTextTooLong_truncated() {
        var message = new Message();
        message.setText("x".repeat(1500));
        var update = new Update();
        update.setMessage(message);

        service.saveHistoryAction(user(1L), update);

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getMessageText()).hasSize(999);
    }

    @Test
    void saveHistoryAction_mainMenu_notSaved() {
        var message = new Message();
        message.setText("Главное меню");
        var update = new Update();
        update.setMessage(message);

        service.saveHistoryAction(user(1L), update);

        verify(repository, never()).save(any());
    }

    @Test
    void saveHistoryAction_document_savesCaptionAndFileName() {
        var document = new Document();
        var message = new Message();
        message.setDocument(document);
        message.setCaption("caption");
        message.setText("file.xlsx");
        var update = new Update();
        update.setMessage(message);

        service.saveHistoryAction(user(1L), update);

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getMessageText()).isEqualTo("caption");
        assertThat(captor.getValue().getFileName()).isEqualTo("file.xlsx");
    }

    @Test
    void saveHistoryAction_callbackQuery_savesMenuName() {
        var button = new InlineKeyboardButton();
        button.setCallbackData("cb");
        button.setText("MenuName");
        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(List.of(button)));
        var message = new Message();
        message.setReplyMarkup(markup);
        var callbackQuery = new CallbackQuery();
        callbackQuery.setData("cb");
        callbackQuery.setMessage(message);
        var update = new Update();
        update.setCallbackQuery(callbackQuery);

        service.saveHistoryAction(user(1L), update);

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getCallbackMenuName()).isEqualTo("MenuName");
    }

    @Test
    void saveHistoryAnswerAction_disabled_doesNothing() {
        ReflectionTestUtils.setField(service, "enabled", false);
        service.saveHistoryAnswerAction(user(1L), List.of(new SendMessage()));
        verify(repository, never()).save(any());
    }

    @Test
    void saveHistoryAnswerAction_sendMessage_saved() {
        var answer = new SendMessage();
        answer.setChatId("456");
        answer.setText("hi");

        service.saveHistoryAnswerAction(user(1L), List.of(answer));

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.getActionType()).isEqualTo(HistoryActionType.SYSTEM_ACTION);
        assertThat(saved.getChatIdTo()).isEqualTo(456L);
        assertThat(saved.getMessageText()).isEqualTo("hi");
    }

    @Test
    void saveHistoryAnswerAction_sendMessageMainMenu_skipped() {
        var answer = new SendMessage();
        answer.setChatId("456");
        answer.setText("Главное меню");

        service.saveHistoryAnswerAction(user(1L), List.of(answer));

        verify(repository, never()).save(any());
    }

    @Test
    void saveHistoryAnswerAction_editMessageText_saved() {
        var answer = new EditMessageText();
        answer.setChatId("456");
        answer.setText("edited");

        service.saveHistoryAnswerAction(user(1L), List.of(answer));

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getMessageText()).isEqualTo("edited");
        assertThat(captor.getValue().getChatIdTo()).isEqualTo(456L);
    }

    @Test
    void saveHistoryAnswerAction_editMessageTextMainMenu_skipped() {
        var answer = new EditMessageText();
        answer.setChatId("456");
        answer.setText("Главное меню");

        service.saveHistoryAnswerAction(user(1L), List.of(answer));

        verify(repository, never()).save(any());
    }

    @Test
    void saveWebHistoryActionProtect_nullMessageText_usesEmpty() {
        var details = new CustomUserDetails("789", "p", null, null);

        service.saveWebHistoryActionProtect(details, "file.xlsx", null);

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getMessageText()).isEmpty();
    }

    @Test
    void saveHistoryAnswerAction_sendDocument_savedWithFileName() {
        var answer = new SendDocument();
        answer.setChatId("456");
        answer.setCaption("caption");
        answer.setDocument(new InputFile("doc.xlsx"));

        service.saveHistoryAnswerAction(user(1L), List.of(answer));

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.getMessageText()).isEqualTo("caption");
        assertThat(saved.getFileName()).isEqualTo("doc.xlsx");
        assertThat(saved.getChatIdTo()).isEqualTo(456L);
    }

    @Test
    void saveHistoryAnswerAction_multipleAnswers_savesEach() {
        var one = new SendMessage();
        one.setChatId("1");
        one.setText("one");
        var two = new SendMessage();
        two.setChatId("2");
        two.setText("two");

        service.saveHistoryAnswerAction(user(1L), List.of(one, two));

        verify(repository, times(2)).save(any());
    }

    @Test
    void saveWebHistoryActionProtect_savesForUser() {
        var details = new CustomUserDetails("789", "p", null, null);

        service.saveWebHistoryActionProtect(details, "file.xlsx", "message");

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.getActionType()).isEqualTo(HistoryActionType.WEB_ACTION);
        assertThat(saved.getChatIdFrom()).isEqualTo(789L);
        assertThat(saved.getFileName()).isEqualTo("file.xlsx");
    }

    @Test
    void saveWebHistoryActionProtect_nullUser_skipped() {
        service.saveWebHistoryActionProtect(null, "file.xlsx", "message");
        verify(repository, never()).save(any());
    }

    @Test
    void saveWebHistoryErrorActionProtect_savesWithErrorType() {
        var details = new CustomUserDetails("789", "p", null, null);

        service.saveWebHistoryErrorActionProtect(details, "file.xlsx", "message");

        var captor = ArgumentCaptor.forClass(HistoryAction.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getActionType()).isEqualTo(HistoryActionType.WEB_ERROR_ACTION);
    }

    @Test
    void saveWebHistoryActionProtect_error_disablesService() {
        doThrow(new RuntimeException("db down")).when(repository).save(any());
        var details = new CustomUserDetails("789", "p", null, null);

        service.saveWebHistoryActionProtect(details, "file.xlsx", "message");
        service.saveWebHistoryActionProtect(details, "file.xlsx", "message");

        verify(repository, times(1)).save(any());
    }
}
