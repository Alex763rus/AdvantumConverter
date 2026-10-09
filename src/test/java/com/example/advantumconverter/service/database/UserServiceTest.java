package com.example.advantumconverter.service.database;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.service.menu.StateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StateService stateService;
    @Mock
    private CompanyRepository companyRepository;

    private UserService service;

    private final Company company = new Company();

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository, stateService, companyRepository);
        when(companyRepository.getCompaniesByCompanyName(any())).thenReturn(company);
    }

    private User user(Long chatId) {
        var user = new User();
        user.setChatId(chatId);
        return user;
    }

    private Update messageUpdate(Long chatId) {
        var chat = new Chat();
        chat.setId(chatId);
        chat.setFirstName("First");
        chat.setLastName("Last");
        chat.setUserName("login");
        var message = new Message();
        message.setChat(chat);
        var update = new Update();
        update.setMessage(message);
        return update;
    }

    @Test
    void init_reloadsUsersAsFree() {
        when(userRepository.findAll()).thenReturn(List.of(user(1L), user(2L)));

        service.init();

        verify(stateService, times(2)).setState(any(User.class), eq(State.FREE));
    }

    @Test
    void getUser_fromStateCache() {
        var cached = user(1L);
        when(stateService.getUser(1L)).thenReturn(cached);

        var result = service.getUser(messageUpdate(1L));

        assertThat(result).isSameAs(cached);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void getUser_fromRepository() {
        var stored = user(1L);
        when(stateService.getUser(1L)).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(stored));

        var result = service.getUser(messageUpdate(1L));

        assertThat(result).isSameAs(stored);
    }

    @Test
    void getUser_notFound_registersNewUser() {
        when(stateService.getUser(1L)).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var result = service.getUser(messageUpdate(1L));

        assertThat(result.getChatId()).isEqualTo(1L);
        assertThat(result.getUserName()).isEqualTo("login");
        assertThat(result.getUserRole()).isEqualTo(UserRole.NEED_SETTING);
        assertThat(result.getCompany()).isSameAs(company);
        verify(userRepository).save(any(User.class));
        verify(stateService).setState(any(User.class), eq(State.FREE));
    }

    @Test
    void getUser_callbackQuery_usesCallbackMessage() {
        var cached = user(2L);
        when(stateService.getUser(2L)).thenReturn(cached);
        var chat = new Chat();
        chat.setId(2L);
        var message = new Message();
        message.setChat(chat);
        var callbackQuery = new CallbackQuery();
        callbackQuery.setMessage(message);
        var update = new Update();
        update.setCallbackQuery(callbackQuery);

        var result = service.getUser(update);

        assertThat(result).isSameAs(cached);
    }

    @Test
    void refreshUser_delegatesToStateService() {
        var user = user(1L);
        service.refreshUser(user);
        verify(stateService).refreshUser(user);
    }

    @Test
    void registerNewUser_savesAndSetsFree() {
        var result = service.registerNewUser(5L, "login", "First");

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getChatId()).isEqualTo(5L);
        assertThat(result.getUserRole()).isEqualTo(UserRole.NEED_SETTING);
        verify(stateService).setState(any(User.class), eq(State.FREE));
    }
}
