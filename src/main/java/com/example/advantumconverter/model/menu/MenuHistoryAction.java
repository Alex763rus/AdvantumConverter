package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.jpa.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_HISTORIC_ACTION;
import static com.example.advantumconverter.enums.HistoryActionType.USER_ACTION;
import static com.example.advantumconverter.enums.State.*;
import static com.example.advantumconverter.enums.UserRole.EMPLOYEE;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Component(COMMAND_HISTORIC_ACTION)
@Slf4j
public class MenuHistoryAction extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_HISTORIC_ACTION;
    }

    @Autowired
    private HistoryActionRepository historyActionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> freelogic(user, update);
                case HISTORY_WAIT_USER -> historyWaitUserLogic(user, update);
                case HISTORY_WAIT_COMPANY -> historyWaitCompanyLogic(user, update);
                default -> errorMessageDefault(update);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return errorMessage(update, ex.toString());
        }
    }

    private class HistoryActionDateComparator implements Comparator<HistoryAction> {
        @Override
        public int compare(HistoryAction o1, HistoryAction o2) {
            return o1.getActionDate().compareTo(o2.getActionDate());
        }
    }

    private List<PartialBotApiMethod> historyWaitUserLogic(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val userEmployeeChatId = update.getCallbackQuery().getData();
        val dateStart = DateUtils.addDays(new Date(), -7);
        val userEmployee = userRepository.findUserByChatId(Long.parseLong(userEmployeeChatId));
        val historyActionsFrom = historyActionRepository.findByChatIdFromEqualsAndActionDateAfter(
                Long.parseLong(userEmployeeChatId), dateStart);
        val historyActionsTo = historyActionRepository.findByChatIdToEqualsAndActionDateAfter(
                Long.parseLong(userEmployeeChatId), dateStart);
        val historyActions = new ArrayList<HistoryAction>();
        historyActions.addAll(historyActionsFrom);
        historyActions.addAll(historyActionsTo);
        val comparator = new HistoryActionDateComparator();
        historyActionsTo.stream().sorted(comparator);
        val answer = new StringBuilder();
        answer.append("Действия пользователя: " + prepareShield(userEmployee.getNameOrFirst())).append(SPACE);
        if (historyActionsTo.isEmpty()) {
            answer.append("не найдены");
        }
        answer.append(NEW_LINE);
        for (HistoryAction historyAction : historyActionsTo) {
            answer.append(historyAction.getActionDate()).append(SPACE)
                    .append(historyAction.getActionType() == USER_ACTION ? "исх: " : "вх: ");
            if (historyAction.getMessageText() != null) {
                answer.append(prepareShield(historyAction.getMessageText())).append(SPACE);
            }
            if (historyAction.getCallbackMenuName() != null) {
                answer.append(prepareShield(historyAction.getCallbackMenuName())).append(SPACE);
            }
            if (historyAction.getFileName() != null) {
                answer.append(prepareShield(historyAction.getFileName())).append(SPACE);
            }
            answer.append(NEW_LINE).append(NEW_LINE);
        }
        stateService.setState(user, FREE);
        return createMessageList(user, answer.toString());
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        return switch (user.getUserRole()) {
            case MAIN_EMPLOYEE -> getFreeLogicMainEmployee(user, update);
            case SUPPORT -> gerFreeLogicSupport(user, update);
            default -> errorMessage(update, "У вашей роли не должно быть сюда доступа.." + user.getUserRole());
        };
    }

    private List<PartialBotApiMethod> gerFreeLogicSupport(User user, Update update) {
        val companys = companyRepository.findAll();
        if (companys.isEmpty()) {
            return createMessageList(user, "Компании отсутствуют");
        }
        val buttons = new ArrayList<Button>();
        for (Company company : companys) {
            buttons.add(Button.init().setKey(String.valueOf(company.getCompanyId()))
                    .setValue(prepareShield(company.getCompanyName())).build());
        }
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        stateService.setState(user, HISTORY_WAIT_COMPANY);
        return createMessageList(user, "Выберите компанию:", buttonsDescription);
    }

    private List<PartialBotApiMethod> historyWaitCompanyLogic(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val company = companyRepository.findCompanyByCompanyId(Long.parseLong(update.getCallbackQuery().getData()));
        val users = userRepository.findUserByCompany(company);
        return showUsers(user, users);
    }

    private List<PartialBotApiMethod> getFreeLogicMainEmployee(User user, Update update) {
        val users = userRepository.findUserByCompanyAndAndUserRole(user.getCompany(), EMPLOYEE);
        return showUsers(user, users);
    }

    private List<PartialBotApiMethod> showUsers(User user, List<User> users) {
        if (users.isEmpty()) {
            return createMessageList(user, "В выбранной компании пользователи не найдены");
        }
        stateService.setState(user, HISTORY_WAIT_USER);
        val buttons = new ArrayList<Button>();
        for (User value : users) {
            buttons.add(Button.init().setKey(String.valueOf(value.getChatId()))
                    .setValue(prepareShield(value.getNameOrFirst())).build());
        }
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        return createMessageList(user, "Выберите сотрудника:", buttonsDescription);
    }

    @Override
    public String getDescription() {
        return "Активность пользователя";
    }
}
