package com.example.advantumconverter.config;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SecurityConfigTest {

    @Mock
    private UserRepository userRepository;

    private SecurityConfig config;

    private User user(String login, Long chatId, UserRole role) {
        var company = new Company();
        company.setCompanyId(1L);
        company.setCompanyName("Company");
        var user = new User();
        user.setChatId(chatId);
        user.setUserName(login);
        user.setLogin(login);
        user.setPasswordHash("{noop}12345");
        user.setUserRole(role);
        user.setCompany(company);
        return user;
    }

    @BeforeEach
    void setUp() {
        config = new SecurityConfig();
        ReflectionTestUtils.setField(config, "userRepository", userRepository);
    }

    @Test
    void loadUserByUsername_readsFreshRoleFromDb() {
        when(userRepository.findUserByLogin("alice")).thenReturn(user("alice", 12345L, UserRole.ADMIN));

        var details = config.userDetailsService().loadUserByUsername("alice");

        assertThat(details).isInstanceOf(CustomUserDetails.class);
        assertThat(details.getUsername()).isEqualTo("alice");
        assertThat(details.isEnabled()).isTrue();
        assertThat(details.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_needsSettingUser_disabled() {
        when(userRepository.findUserByLogin("bob")).thenReturn(user("bob", 777L, UserRole.NEED_SETTING));

        var details = config.userDetailsService().loadUserByUsername("bob");

        assertThat(details.isEnabled()).isFalse();
    }

    @Test
    void loadUserByUsername_unknownUser_throws() {
        when(userRepository.findUserByLogin("ghost")).thenReturn(null);

        assertThatThrownBy(() -> config.userDetailsService().loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsername_legacyUserWithoutHash_backfillsPasswordWithChatId() {
        var legacy = user("legacy", 555L, UserRole.ADMIN);
        legacy.setPasswordHash(null);
        when(userRepository.findUserByLogin("legacy")).thenReturn(legacy);

        var details = config.userDetailsService().loadUserByUsername("legacy");

        assertThat(details.getPassword()).isNotBlank();
        assertThat(legacy.getPasswordHash()).isNotBlank();
        verify(userRepository).save(legacy);
    }
}
