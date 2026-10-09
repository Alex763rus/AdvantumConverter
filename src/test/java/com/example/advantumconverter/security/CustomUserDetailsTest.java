package com.example.advantumconverter.security;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUserDetailsTest {

    private Company company() {
        var company = new Company();
        company.setCompanyId(1L);
        company.setCompanyName("Test");
        return company;
    }

    @Test
    void admin_isFullyEnabled() {
        var details = new CustomUserDetails("user", "pass", UserRole.ADMIN, company());

        assertThat(details.getUsername()).isEqualTo("user");
        assertThat(details.getPassword()).isEqualTo("pass");
        assertThat(details.getAuthorities())
                .extracting(authority -> authority.getAuthority())
                .containsExactly("ROLE_ADMIN");
        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
        assertThat(details.isEnabled()).isTrue();
        assertThat(details.getRoleTitle()).isEqualTo(UserRole.ADMIN.getTitle());
    }

    @Test
    void blocked_isLockedAndDisabled() {
        var details = new CustomUserDetails("u", "p", UserRole.BLOCKED, company());

        assertThat(details.isAccountNonLocked()).isFalse();
        assertThat(details.isEnabled()).isFalse();
    }

    @Test
    void needSetting_isDisabledButNotLocked() {
        var details = new CustomUserDetails("u", "p", UserRole.NEED_SETTING, company());

        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isEnabled()).isFalse();
    }
}
