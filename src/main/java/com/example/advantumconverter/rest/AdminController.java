package com.example.advantumconverter.rest;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.service.database.DictionaryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.example.advantumconverter.enums.UserRole.NEED_SETTING;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final DictionaryService dictionaryService;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    // Показать всех пользователей с ролью NEED_SETTING
    @GetMapping("/pending-users")
    public String showPendingUsers(Model model) {
        List<User> pendingUsers = userRepository.findUserByUserRole(NEED_SETTING);
        List<Company> companies = companyRepository.findAll();

        model.addAttribute("pendingUsers", pendingUsers);
        model.addAttribute("companies", companies);
        model.addAttribute("roles", UserRole.values()); // роли, которые можно назначить

        return "admin/pending-users";
    }

    // Обновить пользователя: назначить компанию и роль
    @PostMapping("/pending-users/{id}/assign")
    public String assignCompanyAndRole(
            @PathVariable Long id,
            @RequestParam Long companyId,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Компания не найдена"));

        try {
            UserRole userRole = UserRole.valueOf(role);
            user.setCompany(company);
            user.setUserRole(userRole);
            user.setUserRoletext(userRole.name());
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success",
                    "Пользователь '" + user.getUserName() + "' успешно настроен и активирован");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Неверная роль: " + role);
        }

        return "redirect:/admin/pending-users";
    }

    @GetMapping("/update-dictionaries")
    public String showUpdatePage(Model model) {
        model.addAttribute("message", "Готовы обновить справочники?");
        return "admin/update-dictionaries"; // шаблон
    }

    @PostMapping("/update-dictionaries")
    public String updateDictionaries(RedirectAttributes redirectAttributes) {
        try {
            dictionaryService.reloadDictionary();

            redirectAttributes.addFlashAttribute("success", "Справочники успешно обновлены!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }
        return "redirect:/admin/update-dictionaries";
    }

}
