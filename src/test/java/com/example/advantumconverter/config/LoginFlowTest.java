package com.example.advantumconverter.config;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LoginFlowTest {

    @Mock
    private UserRepository userRepository;

    private SecurityConfig config;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        config = new SecurityConfig();
        ReflectionTestUtils.setField(config, "userRepository", userRepository);
        passwordEncoder = config.passwordEncoder();

        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(config.userDetailsService());
        provider.setPasswordEncoder(passwordEncoder);
        authenticationManager = new ProviderManager(provider);
    }

    private User user(String login, Long chatId, UserRole role, String rawPassword) {
        var company = new Company();
        company.setCompanyId(1L);
        company.setCompanyName("Company");
        var user = new User();
        user.setChatId(chatId);
        user.setUserName(login);
        user.setLogin(login);
        user.setPasswordHash(rawPassword == null ? null : passwordEncoder.encode(rawPassword));
        user.setUserRole(role);
        user.setCompany(company);
        return user;
    }

    @Test
    void registeredUserWithAssignedRole_authenticatesWithRegisteredPassword() {
        when(userRepository.findUserByLogin("alice")).thenReturn(user("alice", 42L, UserRole.EMPLOYEE, "secret123"));

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("alice", "secret123"));

        assertThat(auth.isAuthenticated()).isTrue();
        assertThat(auth.getName()).isEqualTo("alice");
    }

    @Test
    void registeredUser_wrongPassword_fails() {
        when(userRepository.findUserByLogin("alice")).thenReturn(user("alice", 42L, UserRole.EMPLOYEE, "secret123"));

        assertThatThrownBy(() -> authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("alice", "123456")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void newlyRegisteredUser_needSetting_disabled() {
        when(userRepository.findUserByLogin("alice")).thenReturn(user("alice", 42L, UserRole.NEED_SETTING, "secret123"));

        assertThatThrownBy(() -> authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("alice", "secret123")))
                .isInstanceOf(DisabledException.class);
    }

    @Test
    void legacyUser_authenticatesWithChatIdAsPassword() {
        when(userRepository.findUserByLogin("legacy")).thenReturn(user("legacy", 7L, UserRole.EMPLOYEE, null));

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("legacy", "7"));

        assertThat(auth.isAuthenticated()).isTrue();
    }

    @Test
    void resetPasswordUser_authenticatesWithDefaultPassword() {
        var reset = user("alice", 42L, UserRole.EMPLOYEE,
                com.example.advantumconverter.service.database.UserService.DEFAULT_PASSWORD);
        reset.setMustChangePassword(true);
        when(userRepository.findUserByLogin("alice")).thenReturn(reset);

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("alice", "123456"));

        assertThat(auth.isAuthenticated()).isTrue();
    }
}
