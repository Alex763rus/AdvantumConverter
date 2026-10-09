package com.example.advantumconverter.service;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.MenuActivity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SecurityServiceTest {

    private final Company company = company();
    private final MenuActivity menuActivity = mock(MenuActivity.class);
    private SecurityService securityService;

    private static Company company() {
        var company = new Company();
        company.setCompanyId(1L);
        company.setCompanyName("Test");
        return company;
    }

    private User user(UserRole role) {
        var user = new User();
        user.setUserRole(role);
        user.setCompany(company);
        return user;
    }

    @BeforeEach
    void setUp() {
        Map<UserRole, List<String>> roleAccess = new HashMap<>();
        roleAccess.put(UserRole.ADMIN, List.of("cmd", "roleOnly"));
        roleAccess.put(UserRole.EMPLOYEE, List.of("other"));
        Map<Company, List<String>> companyAccess = new HashMap<>();
        companyAccess.put(company, List.of("cmd"));
        Map<String, MenuActivity> mainMenu = new HashMap<>();
        mainMenu.put("cmd", menuActivity);
        securityService = new SecurityService(roleAccess, companyAccess, mainMenu);
    }

    @Test
    void getMenuActivity_returnsRegisteredOrNull() {
        assertThat(securityService.getMenuActivity("cmd")).isSameAs(menuActivity);
        assertThat(securityService.getMenuActivity("unknown")).isNull();
    }

    @Test
    void checkAccess_startAndDefaultAlwaysAllowed() {
        assertThat(securityService.checkAccess(user(UserRole.EMPLOYEE), COMMAND_START)).isTrue();
        assertThat(securityService.checkAccess(user(UserRole.EMPLOYEE), COMMAND_DEFAULT)).isTrue();
    }

    @Test
    void checkAccess_bothRoleAndCompanyAllowed() {
        assertThat(securityService.checkAccess(user(UserRole.ADMIN), "cmd")).isTrue();
    }

    @Test
    void checkAccess_roleNotAllowed() {
        assertThat(securityService.checkAccess(user(UserRole.EMPLOYEE), "cmd")).isFalse();
    }

    @Test
    void checkAccess_companyNotAllowed() {
        assertThat(securityService.checkAccess(user(UserRole.ADMIN), "roleOnly")).isFalse();
    }

    @Test
    void grantApiUser_onlyAdminAndEmployeeApi() {
        assertThat(SecurityService.grantApiUser(user(UserRole.ADMIN))).isTrue();
        assertThat(SecurityService.grantApiUser(user(UserRole.EMPLOYEE_API))).isTrue();
        assertThat(SecurityService.grantApiUser(user(UserRole.EMPLOYEE))).isFalse();
    }
}
