package com.example.advantumconverter.rest;

import com.example.advantumconverter.service.database.DictionaryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class AdminController {

    private final DictionaryService dictionaryService;

    @GetMapping("/admin/update-dictionaries")
    public String showUpdatePage(Model model) {
        model.addAttribute("message", "Готовы обновить справочники?");
        return "admin/update-dictionaries"; // шаблон
    }

    @PostMapping("/admin/update-dictionaries")
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
