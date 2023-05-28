package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.model.menu.*;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class MenuService {

    @Autowired
    private MenuStart menuStart;
    @Autowired
    private MenuDefault menuActivityDefault;

    @Autowired
    private MenuConvertCofix menuConvertCofix;

    @Autowired
    private MenuConvertBogorodsk menuconvertBogorodsk;

    @Autowired
    private MenuConvertScooter menuConvertScooter;
    @Autowired
    private StateService stateService;
    private List<MenuActivity> mainMenu;

    @PostConstruct
    public void mainMenuInit() {
        mainMenu = new ArrayList();
        mainMenu.add(menuStart);
        mainMenu.add(menuConvertCofix);
        mainMenu.add(menuconvertBogorodsk);
        mainMenu.add(menuConvertScooter);
    }

    public List<PartialBotApiMethod> messageProcess(Update update) {
        val chatId = getChatId(update);
        val user = stateService.getUser(chatId);
        if (user == null) {
            log.warn("Отказано в доступе, ChatId: " + chatId);
            return Arrays.asList(
                    SendMessageWrap.init()
                            .setChatIdLong(update.getMessage().getChatId())
                            .setText("Отказано в доступе")
                            .build().createSendMessage());
        }
        MenuActivity menuActivity = null;
        if (update.hasMessage()) {
            for (val menu : mainMenu) {
                if (menu.getMenuName().equals(update.getMessage().getText())) {
                    menuActivity = menu;
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
        mainMenu.stream().forEach(e -> listofCommands.add(new BotCommand(e.getMenuName(), e.getDescription())));
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