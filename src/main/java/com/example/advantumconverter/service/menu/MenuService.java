package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.model.dictionary.security.Security;
import com.example.advantumconverter.model.menu.*;
import com.example.advantumconverter.model.menu.admin.MenuSettingUser;
import com.example.advantumconverter.model.menu.converter.*;
import com.example.advantumconverter.model.menu.support.MenuMyTask;
import com.example.advantumconverter.model.menu.support.MenuOpenTask;
import com.example.advantumconverter.model.menu.support.MenuReloadDictionary;
import com.example.advantumconverter.service.HistoryActionService;
import com.example.advantumconverter.service.database.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.EditMessageTextWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_HISTORIC_ACTION;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_START;
import static com.example.advantumconverter.enums.State.FREE;

@Slf4j
@Service
public class MenuService {

    @Autowired
    private MenuDefault menuActivityDefault;

    @Autowired
    private StateService stateService;

    @Autowired
    private HistoryActionService historyActionService;


    @Autowired
    private MenuHistoryAction menuHistoryAction;

    @Autowired
    private UserService userService;

    @Autowired
    private Security security;

    @Autowired
    private MenuConvertBogorodsk menuConvertBogorodsk;
    @Autowired
    private MenuConvertCofix menuConvertCofix;

    @Autowired
    private MenuConvertAgroprom menuConvertAgroprom;

    @Autowired
    private MenuConvertAgropromDetail menuConvertAgropromDetail;
    @Autowired
    private MenuConvertSamokat menuConvertSamokat;

    @Autowired
    private MenuConvertDominos menuConvertDominos;
    @Autowired
    private MenuConvertLenta menuConvertLenta;

    @Autowired
    private MenuSettingUser menuSettingUser;

    @Autowired
    private MenuOpenTask menuOpenTask;

    @Autowired
    private MenuMyTask menuMyTask;

    @Autowired
    private MenuReloadDictionary menuReloadDictionary;

    @Autowired
    private MenuFaq menuFaq;
    @Autowired
    private MenuStart menuStart;

    @PostConstruct
    public void init() {
        // Список всех возможных обработчиков меню:
        security.setMainMenu(List.of(menuStart, menuConvertBogorodsk, menuConvertCofix, menuConvertSamokat
                        , menuConvertLenta, menuConvertDominos, menuSettingUser, menuFaq, menuOpenTask, menuMyTask, menuHistoryAction
                        , menuReloadDictionary, menuConvertAgroprom, menuConvertAgropromDetail
                )
        );
    }

    public List<PartialBotApiMethod> messageProcess(Update update) {
        val user = userService.getUser(update);
        MenuActivity menuActivity = null;
        if (update.hasMessage()) {
            for (val menu : security.getMainMenu()) {
                if (menu.getMenuComand().equals(update.getMessage().getText())) {
                    if (security.checkAccess(user, menu.getMenuComand())) {
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
        val answer = new ArrayList<PartialBotApiMethod>();
        if (update.hasCallbackQuery()) {
            val message = update.getCallbackQuery().getMessage();
            val menuName = update.getCallbackQuery().getMessage().getReplyMarkup().getKeyboard().stream()
                    .filter(e -> e.get(0).getCallbackData().equals(update.getCallbackQuery().getData()))
                    .findFirst().get().get(0).getText();
            answer.add(EditMessageTextWrap.init()
                    .setChatIdLong(message.getChatId())
                    .setMessageId(message.getMessageId())
                    .setText("Выбрано меню: " + menuName)
                    .build().createMessage());
        }
        answer.addAll(menuActivity.menuRun(user, update));
        if (stateService.getState(user) == FREE && !menuActivity.getMenuComand().equals(menuStart.getMenuComand())) {
            answer.addAll(menuStart.menuRun(user, update));
        }
        try {
            historyActionService.saveHistoryAction(user, update);
            if (!menuActivity.getMenuComand().equals(COMMAND_START) && !menuActivity.getMenuComand().equals(COMMAND_HISTORIC_ACTION)) {
                historyActionService.saveHistoryAnswerAction(user, answer);
            }
        } catch (Exception ex) {
            log.error("Ошибка во время сохранения HistoryAction:" + ex.getMessage());
        }
        return answer;
    }

    public List<BotCommand> getMainMenuComands() {
        val listofCommands = new ArrayList<BotCommand>();
        security.getMainMenu().stream()
                .filter(e -> e.getMenuComand().equals(COMMAND_START))
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