package com.example.advantumconverter.model.menu.admin;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.model.menu.Menu;
import lombok.extern.slf4j.Slf4j;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.val;

import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.*;
import static com.example.advantumconverter.enums.State.*;
import static com.example.advantumconverter.enums.UserRole.*;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Component(COMMAND_SETTING_NEW_USER)
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
                        .build().createMessage(),
                SendMessageWrap.init()
                        .setChatIdLong(userRefresh.getChatId())
                        .setText("Администратор настроил вашу учетную запись" + NEW_LINE
                                + "Установлена роль:" + userRefresh.getUserRole().getTitle() + NEW_LINE
                                + "Нажмите " + COMMAND_START + " для начала работы!")
                        .build().createMessage());
    }

    private List<PartialBotApiMethod> adminSettingWaitCompany(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val company = companyRepository.findCompanyByCompanyId(Long.parseLong(update.getCallbackQuery().getData()));
        val userSetting = userTmp.get(user);
        userSetting.setCompany(company);
        stateService.setState(user, ADMIN_SETTING_WAIT_ROLE);

        val buttons = new ArrayList<Button>();
        buttons.add(Button.init().setKey(BLOCKED.name()).setValue(BLOCKED.getTitle()).build());
        buttons.add(Button.init().setKey(EMPLOYEE.name()).setValue(EMPLOYEE.getTitle()).build());
        buttons.add(Button.init().setKey(MAIN_EMPLOYEE.name()).setValue(MAIN_EMPLOYEE.getTitle()).build());
        buttons.add(Button.init().setKey(SUPPORT.name()).setValue(SUPPORT.getTitle()).build());
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        return createMessageList(user, "Укажите роль:", buttonsDescription);
    }

    private List<PartialBotApiMethod> adminSettingUsernameLogic(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val userSetting = userRepository.findUserByChatId(Long.parseLong(update.getCallbackQuery().getData()));
        userTmp.put(user, userSetting);
        val company = companyRepository.findAll();
        val buttons = new ArrayList<Button>();
        for (Company value : company) {
            buttons.add(Button.init().setKey(String.valueOf(value.getCompanyId()))
                    .setValue(value.getCompanyName()).build());
        }
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        stateService.setState(user, ADMIN_SETTING_WAIT_COMPANY);
        return createMessageList(user, "Укажите компанию:", buttonsDescription);
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val users = userRepository.findUserByUserRole(NEED_SETTING);
        if ((users).isEmpty()) {
            return createMessageList(user, "Отсутствуют контакты, требующие настройки");
        }
        val buttons = new ArrayList<Button>();
        for (User value : users) {
            buttons.add(Button.init().setKey(String.valueOf(value.getChatId()))
                    .setValue(value.getNameOrFirst()).build());
        }
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        stateService.setState(user, ADMIN_SETTING_WAIT_USERNAME);
        return createMessageList(user, "Настроить доступ контакту:", buttonsDescription);
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
