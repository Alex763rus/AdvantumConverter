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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuOpenTaskTest {

    @Mock
    private StateService stateService;
    @Mock
    private SupportTaskRepository supportTaskRepository;
    @Mock
    private FileUploadService fileUploadService;

    private MenuOpenTask menu;

    private static User user(long chatId) {
        var u = new User();
        u.setChatId(chatId);
        return u;
    }

    private static SupportTask task(long id, Long supportChatId) {
        var task = new SupportTask();
        task.setSupportTaskId(id);
        task.setSupportChatId(supportChatId);
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
        menu = new MenuOpenTask();
        ReflectionTestUtils.setField(menu, "stateService", stateService);
        ReflectionTestUtils.setField(menu, "supportTaskRepository", supportTaskRepository);
        ReflectionTestUtils.setField(menu, "fileUploadService", fileUploadService);
    }

    @Test
    void comandAndDescription() {
        assertThat(menu.getMenuComand()).isEqualTo("/show_open_task");
        assertThat(menu.getDescription()).isEqualTo("/show_open_task");
    }

    @Test
    void freelogic_empty() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.FREE);
        when(supportTaskRepository.findByTaskState(SupportTaskState.NEW)).thenReturn(List.of());
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void freelogic_withTasks() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.FREE);
        when(supportTaskRepository.findByTaskState(SupportTaskState.NEW)).thenReturn(List.of(task(10L, null)));
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
        verify(stateService).setState(u, State.SUPPORT_WAIT_CHOOSE_TASK);
    }

    @Test
    void chooseTask_available() throws Exception {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_CHOOSE_TASK);
        when(supportTaskRepository.findById(10L)).thenReturn(Optional.of(task(10L, null)));
        when(fileUploadService.uploadFileFromServer(any())).thenReturn(new File("c:/tmp/file.xlsx"));
        assertThat(menu.menuRun(u, callbackUpdate(1L, "10"))).hasSize(2);
        verify(supportTaskRepository).save(any(SupportTask.class));
        verify(stateService).setState(u, State.FREE);
    }

    @Test
    void chooseTask_alreadyTaken() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_CHOOSE_TASK);
        when(supportTaskRepository.findById(10L)).thenReturn(Optional.of(task(10L, 999L)));
        when(supportTaskRepository.findByTaskState(SupportTaskState.NEW)).thenReturn(List.of());
        assertThat(menu.menuRun(u, callbackUpdate(1L, "10"))).isNotEmpty();
    }

    @Test
    void chooseTask_noCallback() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.SUPPORT_WAIT_CHOOSE_TASK);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
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
