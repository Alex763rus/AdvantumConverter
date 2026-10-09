package com.example.advantumconverter.security;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConverterAccessServiceTest {

    private final Company company = company();
    private final CompanySetting companySetting = new CompanySetting();
    private ConverterAccessService service;

    private static Company company() {
        var company = new Company();
        company.setCompanyId(1L);
        company.setCompanyName("Test");
        return company;
    }

    private CustomUserDetails details(UserRole role, Company company) {
        return new CustomUserDetails("user", "pass", role, company);
    }

    @BeforeEach
    void setUp() {
        Map<UserRole, List<String>> roleAccess = new java.util.HashMap<>();
        roleAccess.put(UserRole.ADMIN, List.of("cmd", "roleOnly"));
        roleAccess.put(UserRole.EMPLOYEE, List.of("other"));
        Map<Company, List<String>> companyAccess = new java.util.HashMap<>();
        companyAccess.put(company, List.of("cmd"));
        service = new ConverterAccessService(companySetting, roleAccess, companyAccess);
    }

    private com.example.advantumconverter.service.excel.converter.ConvertService converter(
            String command, String name, boolean v2) {
        var converter = mock(com.example.advantumconverter.service.excel.converter.ConvertService.class);
        when(converter.getConverterCommand()).thenReturn(command);
        when(converter.getConverterName()).thenReturn(name);
        when(converter.isV2()).thenReturn(v2);
        return converter;
    }

    @Test
    void getAvailableFormats_nullRoleOrCompany_returnsEmpty() {
        var auth = mock(org.springframework.security.core.Authentication.class);
        when(auth.getPrincipal()).thenReturn(details(null, company));
        assertThat(service.getAvailableFormats(auth)).isEmpty();

        when(auth.getPrincipal()).thenReturn(details(UserRole.ADMIN, null));
        assertThat(service.getAvailableFormats(auth)).isEmpty();
    }

    @Test
    void getAvailableFormats_returnsOnlyV2AndAllowed_sorted() {
        var allowed = converter("cmd", "Beta", true);
        var notV2 = converter("cmd", "Gamma", false);
        var notAllowed = converter("roleOnly", "Alpha", true);
        companySetting.setCompanyConverter(Map.of(company, List.of(allowed, notV2, notAllowed)));

        var auth = mock(org.springframework.security.core.Authentication.class);
        when(auth.getPrincipal()).thenReturn(details(UserRole.ADMIN, company));

        var result = service.getAvailableFormats(auth);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("cmd");
        assertThat(result.get(0).getLabel()).isEqualTo("Beta");
    }

    @Test
    void checkAccess_startAndDefaultAlwaysAllowed() {
        assertThat(service.checkAccess(UserRole.ADMIN, COMMAND_START, company)).isTrue();
        assertThat(service.checkAccess(UserRole.ADMIN, COMMAND_DEFAULT, company)).isTrue();
    }

    @Test
    void checkAccess_roleNotAllowed() {
        assertThat(service.checkAccess(UserRole.EMPLOYEE, "cmd", company)).isFalse();
    }

    @Test
    void checkAccess_companyNotAllowed() {
        assertThat(service.checkAccess(UserRole.ADMIN, "roleOnly", company)).isFalse();
    }

    @Test
    void checkAccess_bothAllowed() {
        assertThat(service.checkAccess(UserRole.ADMIN, "cmd", company)).isTrue();
    }

    @Test
    void shouldShowSendToCrm_onlyAdminAndEmployeeApi() {
        var admin = mock(org.springframework.security.core.Authentication.class);
        when(admin.getPrincipal()).thenReturn(details(UserRole.ADMIN, company));
        assertThat(service.shouldShowSendToCrm(admin)).isTrue();

        var api = mock(org.springframework.security.core.Authentication.class);
        when(api.getPrincipal()).thenReturn(details(UserRole.EMPLOYEE_API, company));
        assertThat(service.shouldShowSendToCrm(api)).isTrue();

        var employee = mock(org.springframework.security.core.Authentication.class);
        when(employee.getPrincipal()).thenReturn(details(UserRole.EMPLOYEE, company));
        assertThat(service.shouldShowSendToCrm(employee)).isFalse();
    }

    @Test
    void shouldShowSendToCrm_nullPrincipal_returnsFalse() {
        var auth = mock(org.springframework.security.core.Authentication.class);
        when(auth.getPrincipal()).thenReturn(null);
        assertThat(service.shouldShowSendToCrm(auth)).isFalse();
    }

    @Test
    void getConverter_delegatesToCompanySetting() {
        var converter = converter("cmd", "Beta", true);
        companySetting.setCompanyConverter(Map.of(company, List.of(converter)));

        assertThat(service.getConverter("cmd")).isPresent();
        assertThat(service.getConverter("cmd").get().getConverterCommand()).isEqualTo("cmd");
        assertThat(service.getConverter("unknown")).isEmpty();
    }

    @Test
    void isConversionAllowed_alwaysTrue() {
        assertThat(service.isConversionAllowed("anything")).isTrue();
    }

    @Test
    void getCurrentUserRole_variants() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        assertThat((Object) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "getCurrentUserRole")).isNull();

        var notAuth = mock(org.springframework.security.core.Authentication.class);
        when(notAuth.isAuthenticated()).thenReturn(false);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(notAuth);
        assertThat((Object) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "getCurrentUserRole")).isNull();

        var auth = mock(org.springframework.security.core.Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(details(UserRole.ADMIN, company));
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
        assertThat((Object) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "getCurrentUserRole")).isEqualTo(UserRole.ADMIN);

        var other = mock(org.springframework.security.core.Authentication.class);
        when(other.isAuthenticated()).thenReturn(true);
        when(other.getPrincipal()).thenReturn("string-principal");
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(other);
        assertThat((Object) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "getCurrentUserRole")).isNull();

        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }

    @Test
    void toDisplayName_variants() {
        assertThat((String) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "toDisplayName", "csv")).isEqualTo("CSV");
        assertThat((String) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "toDisplayName", "json")).isEqualTo("JSON");
        assertThat((String) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "toDisplayName", "xml")).isEqualTo("XML");
        assertThat((String) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "toDisplayName", "xlsx")).isEqualTo("xlsx");
        assertThat((String) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(service, "toDisplayName", "pdf")).isEqualTo("PDF");
    }
}
