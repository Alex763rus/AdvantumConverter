package com.example.advantumconverter.service.database;

import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.service.menu.StateService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceBranchTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final StateService stateService = mock(StateService.class);
    private final CompanyRepository companyRepository = mock(CompanyRepository.class);

    private UserService service() {
        return new UserService(userRepository, stateService, companyRepository);
    }

    @Test
    void getMessage_noMessageNorCallback_returnsNull() {
        assertThat((Message) ReflectionTestUtils.invokeMethod(service(), "getMessage", new Update())).isNull();
    }

    @Test
    void registerNewUser_saveThrows_swallowed() {
        when(userRepository.save(any())).thenThrow(new RuntimeException("db down"));
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        var service = service();

        assertThat((com.example.advantumconverter.model.jpa.User)
                ReflectionTestUtils.invokeMethod(service, "registerNewUser", message)).isNotNull();
    }
}
