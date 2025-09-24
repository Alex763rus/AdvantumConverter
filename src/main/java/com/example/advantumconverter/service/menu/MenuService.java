package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.model.menu.MenuActivity;
import com.example.advantumconverter.model.menu.MenuDefault;
import com.example.advantumconverter.model.menu.MenuStart;
import com.example.advantumconverter.service.HistoryActionService;
import com.example.advantumconverter.service.SecurityService;
import com.example.advantumconverter.service.database.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.constant.Constant;
import org.example.tgcommons.model.wrapper.EditMessageTextWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

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
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private MenuStart menuStart;

    @LogExecutionTime(value = "Полный цикл обработки", unit = LogExecutionTime.TimeUnit.SECONDS)
    public List<PartialBotApiMethod> messageProcess(Update update) {
        val user = userService.getUser(update);
        MenuActivity menuActivity = null;
        if (update.hasMessage()) {
            val menu = securityService.getMenuActivity(update.getMessage().getText());
            if (menu != null) {
                menuActivity = securityService.checkAccess(user, menu.getMenuComand()) ? menu : menuActivityDefault;
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
            if (message.getText() != null) {
                val menuName = update.getCallbackQuery().getMessage().getReplyMarkup().getKeyboard().stream()
                        .filter(e -> e.get(0).getCallbackData().equals(update.getCallbackQuery().getData()))
                        .findFirst().get().get(0).getText();
                answer.add(EditMessageTextWrap.init()
                        .setChatIdLong(message.getChatId())
                        .setMessageId(message.getMessageId())
                        .setText("Выбрано меню: " + menuName)
                        .build().createMessage());
            }

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
        val menu = securityService.getMenuActivity(Constant.Command.COMMAND_START);
        return List.of(new BotCommand(menu.getMenuComand(), menu.getDescription()));
    }

}