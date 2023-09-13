package com.example.advantumconverter.service;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.MenuActivity;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;

@Service
@NoArgsConstructor
public class SecurityService {

    @Autowired
    private Map<UserRole, List<String>> roleAccess;

    @Autowired
    private Map<Company, List<String>> companyAccessList;

    @Autowired
    private Map<String, MenuActivity> mainMenu;

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
}