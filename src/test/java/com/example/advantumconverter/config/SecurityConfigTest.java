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

    private User user(Long chatId, UserRole role) {
        var company = new Company();
        company.setCompanyId(1L);
        company.setCompanyName("Company");
        var user = new User();
        user.setChatId(chatId);
        user.setUserName("user" + chatId);
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
        when(userRepository.findUserByChatId(12345L)).thenReturn(user(12345L, UserRole.ADMIN));

        var details = config.userDetailsService().loadUserByUsername("12345");

        assertThat(details).isInstanceOf(CustomUserDetails.class);
        assertThat(details.getUsername()).isEqualTo("12345");
        assertThat(details.isEnabled()).isTrue();
        assertThat(details.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_needsSettingUser_disabled() {
        when(userRepository.findUserByChatId(777L)).thenReturn(user(777L, UserRole.NEED_SETTING));

        var details = config.userDetailsService().loadUserByUsername("777");

        assertThat(details.isEnabled()).isFalse();
    }

    @Test
    void loadUserByUsername_unknownUser_throws() {
        when(userRepository.findUserByChatId(999L)).thenReturn(null);

        assertThatThrownBy(() -> config.userDetailsService().loadUserByUsername("999"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsername_notANumber_throws() {
        assertThatThrownBy(() -> config.userDetailsService().loadUserByUsername("admin"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsername_legacyUserWithoutHash_backfillsPassword() {
        var legacy = user(555L, UserRole.ADMIN);
        legacy.setPasswordHash(null);
        when(userRepository.findUserByChatId(555L)).thenReturn(legacy);

        var details = config.userDetailsService().loadUserByUsername("555");

        assertThat(details.getPassword()).isNotBlank();
        assertThat(legacy.getPasswordHash()).isNotBlank();
        verify(userRepository).save(legacy);
    }
}
