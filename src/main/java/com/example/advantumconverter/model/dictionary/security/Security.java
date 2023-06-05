package com.example.advantumconverter.model.dictionary.security;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.MenuActivity;
import lombok.*;

import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_DEFAULT;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Security {

    private Map<UserRole, List<String>> roleAccess;
    private Map<Company, List<String>> companyAccessList;

    private List<MenuActivity> mainMenu;

    public boolean checkAccess(User user, String menuComand) {
        if (menuComand.equals(COMMAND_START) || menuComand.equals(COMMAND_DEFAULT)) {
            return true;
        }
        val isRoleAccess = roleAccess.get(user.getUserRole()).contains(menuComand);
        val isCompanyAccess = companyAccessList.get(user.getCompany()).contains(menuComand);
        return isRoleAccess && isCompanyAccess;
    }
}
