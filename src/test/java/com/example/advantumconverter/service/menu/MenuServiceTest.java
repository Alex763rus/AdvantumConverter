package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.MenuActivity;
import com.example.advantumconverter.model.menu.MenuDefault;
import com.example.advantumconverter.model.menu.MenuStart;
import com.example.advantumconverter.service.HistoryActionService;
import com.example.advantumconverter.service.SecurityService;
import com.example.advantumconverter.service.database.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;
import static com.example.advantumconverter.enums.State.FREE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDefault menuDefault;
    @Mock
    private StateService stateService;
    @Mock
    private HistoryActionService historyActionService;
    @Mock
    private UserService userService;
    @Mock
    private SecurityService securityService;
    @Mock
    private MenuStart menuStart;
    @Mock
    private BotConfig botConfig;

    private MenuService menuService;

    private static User user(long chatId) {
        var u = new User();
        u.setChatId(chatId);
        return u;
    }

    private static Update messageUpdate(String text) {
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setText(text);
        var update = new Update();
        update.setMessage(message);
        return update;
    }

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDefault, stateService, historyActionService,
                userService, securityService, menuStart, botConfig);
    }

    @Test
    void messageProcess_userNull_sendsAdminError() {
        var update = messageUpdate("/start");
        when(userService.getUser(update)).thenReturn(null);
        when(botConfig.getAdminChatId()).thenReturn("123");

        assertThat(menuService.messageProcess(update)).hasSize(1);
    }

    @Test
    void messageProcess_commandMenuAccess() {
        var update = messageUpdate("/convert");
        var user = user(1L);
        var menu = org.mockito.Mockito.mock(MenuActivity.class);
        when(userService.getUser(update)).thenReturn(user);
        when(securityService.getMenuActivity("/convert")).thenReturn(menu);
        when(menu.getMenuComand()).thenReturn("/convert");
        when(securityService.checkAccess(user, "/convert")).thenReturn(true);
        when(menu.menuRun(user, update)).thenReturn(List.of());
        when(stateService.getState(user)).thenReturn(FREE);
        when(menuStart.getMenuComand()).thenReturn(COMMAND_START);
        when(menuStart.menuRun(user, update)).thenReturn(List.of());

        assertThat(menuService.messageProcess(update)).isNotNull();
        verify(historyActionService).saveHistoryAction(user, update);
        verify(historyActionService).saveHistoryAnswerAction(eq(user), anyList());
    }

    @Test
    void messageProcess_commandMenuNoAccess_usesDefault() {
        var update = messageUpdate("/convert");
        var user = user(1L);
        var menu = org.mockito.Mockito.mock(MenuActivity.class);
        when(userService.getUser(update)).thenReturn(user);
        when(securityService.getMenuActivity("/convert")).thenReturn(menu);
        when(menu.getMenuComand()).thenReturn("/convert");
        when(securityService.checkAccess(user, "/convert")).thenReturn(false);
        when(menuDefault.getMenuComand()).thenReturn("/default");
        when(menuDefault.menuRun(user, update)).thenReturn(List.of());
        when(stateService.getState(user)).thenReturn(FREE);
        when(menuStart.getMenuComand()).thenReturn("/default");

        assertThat(menuService.messageProcess(update)).isNotNull();
    }

    @Test
    void messageProcess_noCommand_usesStateMenu() {
        var update = messageUpdate("/unknown");
        var user = user(1L);
        var stored = org.mockito.Mockito.mock(MenuActivity.class);
        when(userService.getUser(update)).thenReturn(user);
        when(securityService.getMenuActivity("/unknown")).thenReturn(null);
        when(stateService.getMenu(user)).thenReturn(stored);
        when(stored.getMenuComand()).thenReturn("/stored");
        when(stored.menuRun(user, update)).thenReturn(List.of());
        when(stateService.getState(user)).thenReturn(FREE);
        when(menuStart.getMenuComand()).thenReturn(COMMAND_START);
        when(menuStart.menuRun(user, update)).thenReturn(List.of());

        assertThat(menuService.messageProcess(update)).isNotNull();
        verify(stored).menuRun(user, update);
    }

    @Test
    void messageProcess_noCommand_noStateMenu_usesDefault() {
        var update = messageUpdate("/unknown");
        var user = user(1L);
        when(userService.getUser(update)).thenReturn(user);
        when(securityService.getMenuActivity("/unknown")).thenReturn(null);
        when(stateService.getMenu(user)).thenReturn(null);
        when(menuDefault.getMenuComand()).thenReturn("/default");
        when(menuDefault.menuRun(user, update)).thenReturn(List.of());
        when(stateService.getState(user)).thenReturn(FREE);
        when(menuStart.getMenuComand()).thenReturn(COMMAND_START);
        when(menuStart.menuRun(user, update)).thenReturn(List.of());

        assertThat(menuService.messageProcess(update)).isNotNull();
    }

    @Test
    void messageProcess_historyThrows_disablesHistory() {
        var update = messageUpdate("/convert");
        var user = user(1L);
        var menu = org.mockito.Mockito.mock(MenuActivity.class);
        when(userService.getUser(update)).thenReturn(user);
        when(securityService.getMenuActivity("/convert")).thenReturn(menu);
        when(menu.getMenuComand()).thenReturn("/convert");
        when(securityService.checkAccess(user, "/convert")).thenReturn(true);
        when(menu.menuRun(user, update)).thenReturn(List.of());
        when(stateService.getState(user)).thenReturn(FREE);
        when(menuStart.getMenuComand()).thenReturn(COMMAND_START);
        when(menuStart.menuRun(user, update)).thenReturn(List.of());
        doThrow(new RuntimeException("db")).when(historyActionService).saveHistoryAction(user, update);
        when(botConfig.getAdminChatId()).thenReturn("123");

        assertThat(menuService.messageProcess(update)).isNotEmpty();
        verify(historyActionService).disabled();
    }

    @Test
    void messageProcess_callbackQuery_addsEditMessage() {
        var user = user(1L);
        var menu = org.mockito.Mockito.mock(MenuActivity.class);

        var button = new InlineKeyboardButton();
        button.setText("Menu");
        button.setCallbackData("cb");
        var keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(List.of(List.of(button)));

        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setText("some text");
        message.setMessageId(5);
        message.setReplyMarkup(keyboard);

        var callback = new CallbackQuery();
        callback.setData("cb");
        callback.setMessage(message);
        var update = new Update();
        update.setCallbackQuery(callback);

        when(userService.getUser(update)).thenReturn(user);
        when(stateService.getMenu(user)).thenReturn(menu);
        when(menu.getMenuComand()).thenReturn("/stored");
        when(menu.menuRun(user, update)).thenReturn(List.of());
        when(stateService.getState(user)).thenReturn(FREE);
        when(menuStart.getMenuComand()).thenReturn(COMMAND_START);
        when(menuStart.menuRun(user, update)).thenReturn(List.of());

        assertThat(menuService.messageProcess(update)).isNotEmpty();
    }

    @Test
    void getMainMenuComands_returnsStart() {
        var menu = org.mockito.Mockito.mock(MenuActivity.class);
        when(securityService.getMenuActivity(COMMAND_START)).thenReturn(menu);
        when(menu.getMenuComand()).thenReturn(COMMAND_START);
        when(menu.getDescription()).thenReturn("Начало");

        assertThat(menuService.getMainMenuComands()).hasSize(1);
    }
}
