package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.dictionary.security.Security;
import com.example.advantumconverter.model.menu.*;
import com.example.advantumconverter.service.database.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;

@Slf4j
@Service
public class MenuService {

    @Autowired
    private MenuStart menuStart;
    @Autowired
    private MenuDefault menuActivityDefault;

    @Autowired
    private MenuConvertBogorodsk menuConvertBogorodsk;
    @Autowired
    private MenuConvertCofix menuConvertCofix;
    @Autowired
    private MenuConvertSamokat menuConvertSamokat;

    @Autowired
    private MenuConvertDominos menuConvertDominos;
    @Autowired
    private MenuConvertLenta menuConvertLenta;

    @Autowired
    private MenuSettingUser menuSettingUser;

    @Autowired
    private MenuFaq menuFaq;

    @Autowired
    private StateService stateService;
    private List<MenuActivity> mainMenu;
    @Autowired
    private UserService userService;

    @Autowired
    private Security security;

    @Autowired
    private CompanySetting companySetting;

    @PostConstruct
    public void mainMenuInit() {
        mainMenu = new ArrayList();
        mainMenu.add(menuStart);
        mainMenu.add(menuConvertBogorodsk);
        mainMenu.add(menuConvertCofix);
        mainMenu.add(menuConvertSamokat);
        mainMenu.add(menuConvertLenta);
        mainMenu.add(menuConvertDominos);
        mainMenu.add(menuSettingUser);
        mainMenu.add(menuFaq);
    }

    public List<PartialBotApiMethod> messageProcess(Update update) {
        val user = userService.getUser(update);
        MenuActivity menuActivity = null;
        if (update.hasMessage()) {
            for (val menu : mainMenu) {
                if (menu.getMenuComand().equals(update.getMessage().getText())) {
                    if(security.checkAccess(user, menu.getMenuComand())){
                        menuActivity = menu;
                    } else {
                        menuActivity = menuActivityDefault;
                    }
                }
            }
        }
        if (menuActivity != null) {
            stateService.setMenu(user, menuActivity);
        } else {
            menuActivity = stateService.getMenu(user);
            if (menuActivity == null) {
                log.warn("Не найдена команда с именем: " + update.getMessage().getText());
                menuActivity = menuActivityDefault;
            }
        }
        return menuActivity.menuRun(user, update);
    }

    public List<BotCommand> getMainMenuComands() {
        val listofCommands = new ArrayList<BotCommand>();
        mainMenu.stream()
                .filter(e->e.getMenuComand().equals(COMMAND_START))
                .forEach(e -> listofCommands.add(new BotCommand(e.getMenuComand(), e.getDescription())));
        return listofCommands;
    }

    private String getChatId(Update update) {
        if (update.hasMessage()) {
            return String.valueOf(update.getMessage().getChatId());
        }
        if (update.hasCallbackQuery()) {
            return String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        }
        return null;
    }
}