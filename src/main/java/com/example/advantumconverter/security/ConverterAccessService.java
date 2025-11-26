package com.example.advantumconverter.security;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;

@Service
@AllArgsConstructor
public class ConverterAccessService {

    private final CompanySetting companySetting;
    private final Map<UserRole, List<String>> roleAccess;
    private final Map<Company, List<String>> companyAccessList;

    public List<ConverterFormat> getAvailableFormats(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Company company = userDetails.getCompany();
        UserRole userRole = userDetails.getRole();
        if (Objects.isNull(userRole) || Objects.isNull(company)) {
            return Collections.emptyList();
        }
        return companySetting.getConverters(company).stream()
                .filter(ConvertService::isV2) //только v2 будет поддерживаться
                .filter(e->checkAccess(userRole, e.getConverterCommand(), company))
                .map(converter -> new ConverterFormat(converter.getConverterCommand(), converter.getConverterName()))
                .sorted(Comparator.comparing(ConverterFormat::getLabel))
                .collect(Collectors.toList());
    }

    public boolean checkAccess(UserRole userRole, String menuComand, Company company) {
        if (menuComand.equals(COMMAND_START) || menuComand.equals(COMMAND_DEFAULT)) {
            return true;
        }
        val isRoleAccess = roleAccess.get(userRole).contains(menuComand);
        if(!isRoleAccess) {
            return false;
        }
        return  companyAccessList.get(company).contains(menuComand);
    }


    public boolean shouldShowSendToCrm(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails == null) return false;

        UserRole role = userDetails.getRole();
        return role == UserRole.ADMIN || role == UserRole.EMPLOYEE_API;
    }

    public Optional<? extends ConvertService> getConverter(String converterCommand) {
        return companySetting.getConverter(converterCommand);
    }

    public boolean isConversionAllowed(String converterType) {
        return true;
       /*
        UserRole userRole = getCurrentUserRole();
        if (userRole == null) return false;

        List<String> allowed = roleToFormats.getOrDefault(userRole, Collections.emptyList());
        return allowed.stream().anyMatch(t -> t.equalsIgnoreCase(converterType));

        */
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
            case "xlsx" -> "xlsx";
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