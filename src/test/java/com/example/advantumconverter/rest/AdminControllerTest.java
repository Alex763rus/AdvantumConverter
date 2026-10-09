package com.example.advantumconverter.rest;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.service.database.DictionaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminControllerTest {

    @Mock
    private DictionaryService dictionaryService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyRepository companyRepository;

    private AdminController controller;

    private Company company(Long id) {
        var company = new Company();
        company.setCompanyId(id);
        company.setCompanyName("Company" + id);
        return company;
    }

    private User user(Long chatId, String userName, String firstName, Company company, UserRole role) {
        var user = new User();
        user.setChatId(chatId);
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setCompany(company);
        user.setUserRole(role);
        return user;
    }

    @BeforeEach
    void setUp() {
        controller = new AdminController(dictionaryService, userRepository, companyRepository);
    }

    @Test
    void assignCompanyAndRole_success() {
        var user = user(1L, "alice", "Al", company(1L), UserRole.NEED_SETTING);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(companyRepository.findById(2L)).thenReturn(Optional.of(company(2L)));
        RedirectAttributes attributes = mock(RedirectAttributes.class);

        var view = controller.assignCompanyAndRole(1L, 2L, "ADMIN", attributes);

        assertThat(view).isEqualTo("redirect:/admin/pending-users");
        assertThat(user.getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(user.getUserRoletext()).isEqualTo("ADMIN");
        verify(userRepository).save(user);
        verify(attributes).addFlashAttribute(eq("success"), any());
    }

    @Test
    void assignCompanyAndRole_invalidRole_flashError() {
        var user = user(1L, "alice", "Al", company(1L), UserRole.NEED_SETTING);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(companyRepository.findById(2L)).thenReturn(Optional.of(company(2L)));
        RedirectAttributes attributes = mock(RedirectAttributes.class);

        controller.assignCompanyAndRole(1L, 2L, "NOT_A_ROLE", attributes);

        verify(attributes).addFlashAttribute(eq("error"), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void assignCompanyAndRole_userNotFound_throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        RedirectAttributes attributes = mock(RedirectAttributes.class);

        assertThatThrownBy(() -> controller.assignCompanyAndRole(1L, 2L, "ADMIN", attributes))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void assignCompanyAndRole_companyNotFound_throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user(1L, "a", "A", company(1L), UserRole.ADMIN)));
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());
        RedirectAttributes attributes = mock(RedirectAttributes.class);

        assertThatThrownBy(() -> controller.assignCompanyAndRole(1L, 2L, "ADMIN", attributes))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void showPendingUsers_noFilters() {
        var users = List.of(
                user(1L, "alice", "Al", company(1L), UserRole.ADMIN),
                user(2L, "bob", "Bo", company(2L), UserRole.EMPLOYEE),
                user(3L, null, "NoCompany", null, UserRole.NEED_SETTING));
        when(userRepository.findAll()).thenReturn(users);
        when(companyRepository.findAll()).thenReturn(List.of());
        Model model = mock(Model.class);

        var view = controller.showPendingUsers(null, null, null, null, null, model);

        assertThat(view).isEqualTo("admin/pending-users");
        verify(model).addAttribute(eq("pendingUsers"), eq(users));
    }

    @Test
    void showPendingUsers_usernameFilter() {
        var users = List.of(
                user(1L, "alice", "Al", company(1L), UserRole.ADMIN),
                user(2L, "bob", "Bo", company(2L), UserRole.EMPLOYEE));
        when(userRepository.findAll()).thenReturn(users);

        controller.showPendingUsers("ALI", null, null, null, null, mock(Model.class));

        verify(userRepository).findAll();
    }

    @Test
    void showPendingUsers_fullNameFilter() {
        when(userRepository.findAll()).thenReturn(List.of(
                user(1L, "alice", "Alice", company(1L), UserRole.ADMIN),
                user(2L, "bob", "Bob", company(2L), UserRole.EMPLOYEE)));

        controller.showPendingUsers(null, "bob", null, null, null, mock(Model.class));

        verify(companyRepository).findAll();
    }

    @Test
    void showPendingUsers_chatIdFilter() {
        when(userRepository.findAll()).thenReturn(List.of(
                user(1L, "a", "A", company(1L), UserRole.ADMIN)));

        controller.showPendingUsers(null, null, 1L, null, null, mock(Model.class));

        verify(companyRepository).findAll();
    }

    @Test
    void showPendingUsers_companyFilter() {
        when(userRepository.findAll()).thenReturn(List.of(
                user(1L, "a", "A", company(1L), UserRole.ADMIN)));

        controller.showPendingUsers(null, null, null, 1L, null, mock(Model.class));

        verify(companyRepository).findAll();
    }

    @Test
    void showPendingUsers_roleFilter_validAndInvalid() {
        when(userRepository.findAll()).thenReturn(List.of(
                user(1L, "a", "A", company(1L), UserRole.ADMIN)));

        controller.showPendingUsers(null, null, null, null, "ADMIN", mock(Model.class));
        controller.showPendingUsers(null, null, null, null, "INVALID_ROLE", mock(Model.class));

        verify(companyRepository, org.mockito.Mockito.times(2)).findAll();
    }

    @Test
    void updateDictionaries_success() {
        var response = controller.updateDictionaries(mock(RedirectAttributes.class));

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(dictionaryService).reloadDictionary();
    }

    @Test
    void updateDictionaries_failure_serverError() {
        doThrow(new RuntimeException("boom")).when(dictionaryService).reloadDictionary();

        var response = controller.updateDictionaries(mock(RedirectAttributes.class));

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody()).isEqualTo("boom");
    }
}
