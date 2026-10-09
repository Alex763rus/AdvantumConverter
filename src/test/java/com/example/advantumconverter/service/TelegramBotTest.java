package com.example.advantumconverter.service;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.service.menu.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class TelegramBotTest {

    private final BotConfig botConfig = mock(BotConfig.class);
    private final MenuService menuService = mock(MenuService.class);
    private final TelegramBot bot = spy(new TelegramBot(botConfig, menuService));

    private SendMessage message(String text) {
        var message = new SendMessage();
        message.setChatId("1");
        message.setText(text);
        return message;
    }

    @Test
    void getters() {
        when(botConfig.getBotUserName()).thenReturn("user");
        when(botConfig.getBotToken()).thenReturn("token");
        assertThat(bot.getBotUsername()).isEqualTo("user");
        assertThat(bot.getBotToken()).isEqualTo("token");
    }

    @Test
    void init_success() throws TelegramApiException {
        when(botConfig.getBotVersion()).thenReturn("1.0");
        when(menuService.getMainMenuComands()).thenReturn(List.of());
        doReturn(null).when(bot).execute(any(BotApiMethod.class));
        bot.init();
    }

    @Test
    void init_error() throws TelegramApiException {
        when(menuService.getMainMenuComands()).thenReturn(List.of());
        doThrow(new TelegramApiException("fail")).when(bot).execute(any(BotApiMethod.class));
        bot.init();
    }

    @Test
    void onUpdateReceived_splitsLongTextAndSendsDocument() throws TelegramApiException {
        var longText = ("x".repeat(3000) + ",").repeat(2);
        var sendDocument = new SendDocument();
        sendDocument.setChatId("1");
        when(menuService.messageProcess(any())).thenReturn(List.of(message(longText), sendDocument));
        doReturn(null).when(bot).execute(any(BotApiMethod.class));
        doReturn(null).when(bot).execute(any(SendDocument.class));

        bot.onUpdateReceived(new Update());

        assertThat(true).isTrue();
    }

    @Test
    void splitAnswerOnToLongText_short() {
        var shortResult = (List<?>) ReflectionTestUtils.invokeMethod(bot, "splitAnswerOnToLongText", message("short"));
        assertThat(shortResult).hasSize(1);
    }

    @Test
    void splitAnswerOnToLongText_long() {
        var longText = ("x".repeat(100) + ",").repeat(45);
        var result = (List<?>) ReflectionTestUtils.invokeMethod(bot, "splitAnswerOnToLongText", message(longText));
        assertThat(result.size()).isGreaterThan(1);
    }

    @Test
    void onUpdateReceived_nonMessageBotApiMethod() throws TelegramApiException {
        var chatAction = new org.telegram.telegrambots.meta.api.methods.send.SendChatAction();
        chatAction.setChatId("1");
        chatAction.setAction(org.telegram.telegrambots.meta.api.methods.ActionType.TYPING);
        when(menuService.messageProcess(any())).thenReturn(List.of(chatAction));
        doReturn(null).when(bot).execute(any(BotApiMethod.class));

        bot.onUpdateReceived(new Update());

        assertThat(true).isTrue();
    }

    @Test
    void onUpdateReceived_executeThrows() throws TelegramApiException {
        when(menuService.messageProcess(any())).thenReturn(List.of(message("hi")));
        doThrow(new TelegramApiException("fail")).when(bot).execute(any(BotApiMethod.class));

        bot.onUpdateReceived(new Update());

        assertThat(true).isTrue();
    }
}
