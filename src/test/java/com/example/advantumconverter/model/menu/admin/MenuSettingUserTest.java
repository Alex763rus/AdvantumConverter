package com.example.advantumconverter.model.menu.admin;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.service.database.UserService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuSettingUserTest {

    @Mock
    private StateService stateService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserService userService;

    private MenuSettingUser menu;

    private static User user(long chatId) {
        var u = new User();
        u.setChatId(chatId);
        u.setUserRole(UserRole.ADMIN);
        return u;
    }

    private static Update messageUpdate(long chatId) {
        var message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText("/setting");
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
        menu = new MenuSettingUser();
        ReflectionTestUtils.setField(menu, "stateService", stateService);
        ReflectionTestUtils.setField(menu, "userRepository", userRepository);
        ReflectionTestUtils.setField(menu, "companyRepository", companyRepository);
        ReflectionTestUtils.setField(menu, "userService", userService);
    }

    @Test
    void menuComandAndDescription() {
        assertThat(menu.getMenuComand()).isEqualTo("/setting_user");
        assertThat(menu.getDescription()).isNotEmpty();
    }

    @Test
    void menuRun_default() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.CONVERT_FILE_LENTA);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void freelogic_noUsers() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.FREE);
        when(userRepository.findUserByUserRole(UserRole.NEED_SETTING)).thenReturn(List.of());
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void freelogic_withUsers() {
        var u = user(1L);
        var candidate = user(2L);
        candidate.setFirstName("Петр");
        when(stateService.getState(u)).thenReturn(State.FREE);
        when(userRepository.findUserByUserRole(UserRole.NEED_SETTING)).thenReturn(List.of(candidate));
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
        verify(stateService).setState(u, State.ADMIN_SETTING_WAIT_USERNAME);
    }

    @Test
    void usernameLogic_selectsCompany() {
        var u = user(1L);
        var candidate = user(2L);
        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_USERNAME);
        when(userRepository.findUserByChatId(2L)).thenReturn(candidate);
        var company = new Company();
        company.setCompanyId(2L);
        company.setCompanyName("Адвантум");
        when(companyRepository.findAll()).thenReturn(List.of(company));
        assertThat(menu.menuRun(u, callbackUpdate(1L, "2"))).isNotEmpty();
        verify(stateService).setState(u, State.ADMIN_SETTING_WAIT_COMPANY);
    }

    @Test
    void usernameLogic_noCallback() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_USERNAME);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void companyLogic_selectsRole() {
        var u = user(1L);
        var candidate = user(2L);
        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_USERNAME);
        when(userRepository.findUserByChatId(2L)).thenReturn(candidate);
        when(companyRepository.findAll()).thenReturn(List.of());
        menu.menuRun(u, callbackUpdate(1L, "2"));

        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_COMPANY);
        var company = new Company();
        company.setCompanyId(2L);
        when(companyRepository.findCompanyByCompanyId(2L)).thenReturn(company);
        assertThat(menu.menuRun(u, callbackUpdate(1L, "2"))).isNotEmpty();
        verify(stateService).setState(u, State.ADMIN_SETTING_WAIT_ROLE);
    }

    @Test
    void roleLogic_savesUser() {
        var u = user(1L);
        var candidate = user(2L);
        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_USERNAME);
        when(userRepository.findUserByChatId(2L)).thenReturn(candidate);
        when(companyRepository.findAll()).thenReturn(List.of());
        menu.menuRun(u, callbackUpdate(1L, "2"));

        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_COMPANY);
        var company = new Company();
        company.setCompanyId(2L);
        when(companyRepository.findCompanyByCompanyId(2L)).thenReturn(company);
        menu.menuRun(u, callbackUpdate(1L, "2"));

        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_ROLE);
        assertThat(menu.menuRun(u, callbackUpdate(1L, "EMPLOYEE"))).hasSize(2);
        verify(userRepository).save(candidate);
        verify(userService).refreshUser(candidate);
        assertThat(candidate.getUserRole()).isEqualTo(UserRole.EMPLOYEE);
    }

    @Test
    void roleLogic_noCallback() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_ROLE);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }

    @Test
    void companyLogic_noCallback() {
        var u = user(1L);
        when(stateService.getState(u)).thenReturn(State.ADMIN_SETTING_WAIT_COMPANY);
        assertThat(menu.menuRun(u, messageUpdate(1L))).isNotEmpty();
    }
}
