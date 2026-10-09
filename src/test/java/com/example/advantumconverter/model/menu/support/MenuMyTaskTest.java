package com.example.advantumconverter.model.menu.support;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.enums.SupportTaskState;
import com.example.advantumconverter.model.jpa.SupportTask;
import com.example.advantumconverter.model.jpa.SupportTaskRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.FileUploadService;
import com.example.advantumconverter.service.menu.StateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuMyTaskTest {

    @Mock
    private StateService stateService;
    @Mock
    private SupportTaskRepository supportTaskRepository;
    @Mock
    private FileUploadService fileUploadService;

    private MenuMyTask menu;

    private static User user(long chatId) {
        var u = new User();
        u.setChatId(chatId);
        return u;
    }

    private static SupportTask task(long id) {
        var task = new SupportTask();
        task.setSupportTaskId(id);
        task.setEmployeeChatId(555L);
        task.setConverterName("Лента");
        task.setErrorText("err");
        task.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        task.setFilePath("c:/tmp/file.xlsx");
        return task;
    }

    private static Update messageUpdate(long chatId) {
        var message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText("text");
        var update = new Update();
        update.setMessage(message);
        return update;
    }

    private static Update documentUpdate(long chatId) {
        var message = new Message();
        message.setChat(new Chat(chatId, "private"));
        var document = new Document();
        document.setFileName("answer.xlsx");
        document.setFileId("file-id");
        message.setDocument(document);
        message.setCaption("решение");
        var update = new Update();
        update.setMessage(message);
        return update;
    }

    private static Update callbackUpdate(long chatId, String data) {
        var message = new Message();
        message.setChat(new Chat(chatId, "private"));
        var callback = new CallbackQuery();
        callback.setData(data);
        callback.setMessage(message);
        var update = new Update();
        update.setCallbackQuery(callback);
        return update;
    }

    @BeforeEach
    void setUp() {
        menu = new MenuMyTask();
        ReflectionTestUtils.setField(menu, "stateService", stateService);
        ReflectionTestUtils.setField(menu, "supportTaskRepository", supportTaskRepository);
        ReflectionTestUtils.setField(menu, "fileUploadService", fileUploadService);
    }

    @Test
    void comandAndDescription() {
        assertThat(menu.getMenuComand()).isEqualTo("/show_my_task");
        assertThat(menu.getDescription()).isEqualTo("/show_my_task");
    }

    @Test
    void freelogic_empty() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.FREE);
        when(supportTaskRepository.findBySupportChatIdAndTaskState(1L, SupportTaskState.IN_PROGRESS)).thenReturn(List.of());
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void freelogic_withTasks() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.FREE);
        when(supportTaskRepository.findBySupportChatIdAndTaskState(1L, SupportTaskState.IN_PROGRESS)).thenReturn(List.of(task(10L)));
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
        verify(stateService).setState(u, State.SUPPORT_WAIT_CHOOSE_TASK);
    }

    @Test
    void chooseTask_sendsInfo() throws Exception {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_CHOOSE_TASK);
        when(supportTaskRepository.findById(10L)).thenReturn(Optional.of(task(10L)));
        when(fileUploadService.uploadFileFromServer("c:/tmp/file.xlsx")).thenReturn(new File("c:/tmp/file.xlsx"));
        assertThat(menu.menuRun(u, callbackUpdate(1L, "10"))).hasSize(3);
        verify(stateService).setState(u, State.SUPPORT_WAIT_MODE_WORK);
    }

    @Test
    void chooseTask_noCallback() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_CHOOSE_TASK);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void modeWork_cancel() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_MODE_WORK);
        assertThat(menu.menuRun(u, callbackUpdate(1L, "CANCEL"))).isEmpty();
        verify(stateService).setState(u, State.FREE);
    }

    @Test
    void modeWork_resolveTask() throws Exception {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_CHOOSE_TASK);
        when(supportTaskRepository.findById(10L)).thenReturn(Optional.of(task(10L)));
        when(fileUploadService.uploadFileFromServer(any())).thenReturn(new File("c:/tmp/file.xlsx"));
        menu.menuRun(u, callbackUpdate(1L, "10"));

        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_MODE_WORK);
        assertThat(menu.menuRun(u, callbackUpdate(1L, "SUPPORT_WAIT_RESOLVE_TASK"))).isNotEmpty();
        verify(stateService).setState(u, State.SUPPORT_WAIT_RESOLVE_MESSAGE);
    }

    @Test
    void modeWork_otherState() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_MODE_WORK);
        assertThat(menu.menuRun(u, callbackUpdate(1L, "FREE"))).isEmpty();
    }

    @Test
    void modeWork_noCallback() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_MODE_WORK);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void resolveMessage_withDocument() throws Exception {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_CHOOSE_TASK);
        when(supportTaskRepository.findById(10L)).thenReturn(Optional.of(task(10L)));
        when(fileUploadService.uploadFileFromServer(any())).thenReturn(new File("c:/tmp/file.xlsx"));
        menu.menuRun(u, callbackUpdate(1L, "10"));

        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_RESOLVE_MESSAGE);
        when(fileUploadService.getFileName(any(), eq("answer.xlsx"))).thenReturn("c:/in/answer.xlsx");
        when(fileUploadService.uploadFileFromTg("c:/in/answer.xlsx", "file-id")).thenReturn(new File("c:/in/answer.xlsx"));
        assertThat(menu.menuRun(u, documentUpdate(1L))).hasSize(3);
        verify(supportTaskRepository).save(any(SupportTask.class));
    }

    @Test
    void resolveMessage_noDocument() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_RESOLVE_MESSAGE);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void resolveMessage_noMessage() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_RESOLVE_MESSAGE);
        var update = org.mockito.Mockito.mock(Update.class);
        when(update.hasMessage()).thenReturn(false);
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        when(update.getMessage()).thenReturn(message);
        assertThat(menu.menuRun(u, update)).isNotEmpty();
    }

    @Test
    void resolveMessage_uploadFails() throws Exception {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_CHOOSE_TASK);
        when(supportTaskRepository.findById(10L)).thenReturn(Optional.of(task(10L)));
        when(fileUploadService.uploadFileFromServer(any())).thenReturn(new File("c:/tmp/file.xlsx"));
        menu.menuRun(u, callbackUpdate(1L, "10"));

        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_RESOLVE_MESSAGE);
        when(fileUploadService.getFileName(any(), eq("answer.xlsx"))).thenReturn("c:/in/answer.xlsx");
        when(fileUploadService.uploadFileFromTg(any(), any())).thenThrow(new RuntimeException("boom"));
        assertThat(menu.menuRun(u, documentUpdate(1L))).isNotEmpty();
    }

    @Test
    void defaultState_error() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.CONVERT_FILE_LENTA);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void exceptionDuringProcessing() {
        var u = user(1L);
        when(stateService.getState(u)).thenThrow(new RuntimeException("boom"));
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }
}
