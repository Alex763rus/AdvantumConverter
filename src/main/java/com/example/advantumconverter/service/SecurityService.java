package com.example.advantumconverter.service;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.MenuActivity;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;
import static com.example.advantumconverter.enums.UserRole.ADMIN;
import static com.example.advantumconverter.enums.UserRole.EMPLOYEE_API;

@Service
@AllArgsConstructor
public class SecurityService {

    private static final Set<UserRole> grantsApiUser = Set.of(EMPLOYEE_API, ADMIN);

    private final Map<UserRole, List<String>> roleAccess;

    private final Map<Company, List<String>> companyAccessList;

    private final Map<String, MenuActivity> mainMenu;

    public MenuActivity getMenuActivity(String commandName) {
        return mainMenu.getOrDefault(commandName, null);
    }

    public boolean checkAccess(User user, String menuComand) {
        if (menuComand.equals(COMMAND_START) || menuComand.equals(COMMAND_DEFAULT)) {
            return true;
        }
        val isRoleAccess = roleAccess.get(user.getUserRole()).contains(menuComand);
        val isCompanyAccess = companyAccessList.get(user.getCompany()).contains(menuComand);
        return isRoleAccess && isCompanyAccess;
    }

    public static boolean grantApiUser(User user) {
        return grantsApiUser.contains(user.getUserRole());
    }
}