package com.example.advantumconverter.model.dictionary.security;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
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

    private Map<UserRole, List<String>> roleAccessList;
    private Map<Company, List<String>> companyAccessList;

    public boolean checkAccess(User user, String menuComand) {
        if(menuComand.equals(COMMAND_START)||menuComand.equals(COMMAND_DEFAULT)){
            return true;
        }
        val roleAccess = roleAccessList.get(user.getUserRole()).contains(menuComand);
        val companyAccess = companyAccessList.get(user.getCompany()).contains(menuComand);
        return roleAccess && companyAccess;
    }
}
