package com.example.advantumconverter.model.menu.admin;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.model.menu.Menu;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.val;

import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_SETTING_NEW_USER;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;
import static com.example.advantumconverter.constant.Constant.NEW_LINE;
import static com.example.advantumconverter.enums.State.*;
import static com.example.advantumconverter.enums.UserRole.*;
import static com.example.advantumconverter.utils.StringUtils.prepareShield;

@Component
@Slf4j
public class MenuSettingUser extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_SETTING_NEW_USER;
    }

    @Autowired
    private UserRepository userRepository;

    private Map<User, User> userTmp = new HashMap();

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freelogic(user, update);
            case ADMIN_SETTING_WAIT_USERNAME:
                return adminSettingUsernameLogic(user, update);
            case ADMIN_SETTING_WAIT_COMPANY:
                return adminSettingWaitCompany(user, update);
            case ADMIN_SETTING_WAIT_ROLE:
                return adminSettingWaitRole(user, update);
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> adminSettingWaitRole(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val userRefresh = userTmp.get(user);
        val userRole = UserRole.valueOf(update.getCallbackQuery().getData());
        userRefresh.setUserRole(userRole);
        userRepository.save(userRefresh);
        userService.refreshUser(userRefresh);
        stateService.setState(user, FREE);
        userTmp.remove(user);
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getCallbackQuery().getMessage().getChatId())
                        .setText("Пользователь успешно настроен:" + prepareShield(userRefresh.toString()))
                        .build().createSendMessage(),
                SendMessageWrap.init()
                        .setChatIdLong(userRefresh.getChatId())
                        .setText("Администратор настроил вашу учетную запись" + NEW_LINE
                                + "Установлена роль:" + userRefresh.getUserRole().getTitle() + NEW_LINE
                                + "Нажмите " + COMMAND_START + " для начала работы!")
                        .build().createSendMessage());
    }

    private List<PartialBotApiMethod> adminSettingWaitCompany(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val company = companyRepository.findCompanyByCompanyId(Long.parseLong(update.getCallbackQuery().getData()));
        val userSetting = userTmp.get(user);
        userSetting.setCompany(company);
        val btns = new LinkedHashMap<String, String>();
        btns.put(BLOCKED.name(), BLOCKED.getTitle());
        btns.put(EMPLOYEE.name(), EMPLOYEE.getTitle());
        btns.put(MAIN_EMPLOYEE.name(), MAIN_EMPLOYEE.getTitle());
        btns.put(SUPPORT.name(), SUPPORT.getTitle());
        stateService.setState(user, ADMIN_SETTING_WAIT_ROLE);
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getCallbackQuery().getMessage().getChatId())
                        .setText("Укажите роль:")
                        .setInlineKeyboardMarkup(buttonService.createVerticalMenu(btns))
                        .build().createSendMessage());
    }

    private List<PartialBotApiMethod> adminSettingUsernameLogic(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val userSetting = userRepository.findUserByChatId(Long.parseLong(update.getCallbackQuery().getData()));
        userTmp.put(user, userSetting);
        val company = companyRepository.findAll();
        val btns = new LinkedHashMap<String, String>();
        for (int i = 0; i < company.size(); ++i) {
            btns.put(String.valueOf(company.get(i).getCompanyId()), company.get(i).getCompanyName());
        }
        stateService.setState(user, ADMIN_SETTING_WAIT_COMPANY);
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getCallbackQuery().getMessage().getChatId())
                        .setText("Укажите компанию:")
                        .setInlineKeyboardMarkup(buttonService.createVerticalMenu(btns))
                        .build().createSendMessage());
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val users = userRepository.findUserByUserRole(NEED_SETTING);
        if ((users).size() == 0) {
            return Arrays.asList(SendMessageWrap.init()
                    .setChatIdLong(user.getChatId())
                    .setText("Отсутствуют контакты, требующие настройки")
                    .build().createSendMessage());
        }
        val btns = new LinkedHashMap<String, String>();
        for (int i = 0; i < users.size(); ++i) {
            btns.put(String.valueOf(users.get(i).getChatId()), users.get(i).getNameOrFirst());
        }
        stateService.setState(user, ADMIN_SETTING_WAIT_USERNAME);
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText("Настроить доступ контакту:")
                        .setInlineKeyboardMarkup(buttonService.createVerticalMenu(btns))
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
