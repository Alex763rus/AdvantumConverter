package com.example.advantumconverter.security;

import com.example.advantumconverter.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConverterAccessService {

    // Маппинг: UserRole → разрешённые форматы
    private final Map<UserRole, List<String>> roleToFormats = Map.of(
            UserRole.EMPLOYEE, List.of("csv"),
            UserRole.MAIN_EMPLOYEE, List.of("csv", "json"),
            UserRole.SUPPORT, List.of("csv", "json"),
            UserRole.ADMIN, List.of("csv", "json", "xml")
            // Остальные роли (BLOCKED, NEED_SETTING, EMPLOYEE_API) — не добавляем
    );

    public List<ConverterFormat> getAvailableFormats() {
        UserRole userRole = getCurrentUserRole();
        if (userRole == null) return Collections.emptyList();

        List<String> allowedTypes = roleToFormats.getOrDefault(userRole, Collections.emptyList());

        return allowedTypes.stream()
                .map(type -> new ConverterFormat(type, toDisplayName(type)))
                .sorted(Comparator.comparing(ConverterFormat::getLabel))
                .collect(Collectors.toList());
    }

    public boolean isConversionAllowed(String converterType) {
        UserRole userRole = getCurrentUserRole();
        if (userRole == null) return false;

        List<String> allowed = roleToFormats.getOrDefault(userRole, Collections.emptyList());
        return allowed.stream().anyMatch(t -> t.equalsIgnoreCase(converterType));
    }

    private UserRole getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getRole();
        }

        return null;
    }

    private String toDisplayName(String type) {
        return switch (type.toLowerCase()) {
            case "csv" -> "CSV";
            case "json" -> "JSON";
            case "xml" -> "XML";
            default -> type.toUpperCase();
        };
    }

    // Вложенный класс для передачи в шаблон
    @Getter
    @AllArgsConstructor
    public static class ConverterFormat {
        private final String type;
        private final String label;
    }
}