package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.*;
import static com.example.advantumconverter.enums.Emoji.BLUSH;
import static org.example.tgcommons.constant.Constant.TextConstants.*;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Component(COMMAND_START)
@Slf4j
@AllArgsConstructor
public class MenuStart extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_START;
    }

    private final CompanySetting companySetting;

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        String messageText;
        switch (user.getUserRole()) {
            case NEED_SETTING -> {
                log.warn("ожидает обработки новый пользователь ChatId:" + update.getMessage().getChatId());
                messageText = "Добрый день, " + prepareShield(user.getNameOrFirst()) + "! " + BLUSH.getCode() + "\n\nВы успешно зарегистрированы в системе.\nОжидайте, после настройки вашего аккаунта вам придет сообщение";
            }
            case BLOCKED -> messageText = "Доступ запрещен";
            case EMPLOYEE -> messageText = getEmployeeMenuText(user);
            case MAIN_EMPLOYEE -> messageText = getMainEmployeeMenuText(user);
            case SUPPORT -> messageText = getSupportMenuText(user);
            case ADMIN -> messageText = getAdminMenuText(user);
            case EMPLOYEE_API -> messageText = getSupportMenuText(user);
            default -> messageText = EMPTY;
        }
        return createMessageList(user, EmojiParser.parseToUnicode(messageText));
    }

    private String getEmployeeMenuText(User user) {
        val menu = new StringBuilder(prepareMainMenu());
        menu.append(prepareAvailableConverters(user.getCompany()));
        return menu.toString();
    }


    private String getMainEmployeeMenuText(User user) {
        val menu = new StringBuilder(getEmployeeMenuText(user));
        menu.append(NEW_LINE)
                .append("- действия сотрудников: ").append(prepareShield(COMMAND_HISTORIC_ACTION)).append(NEW_LINE);
        return menu.toString();
    }

    private String getSupportMenuText(User user) {
        val menu = new StringBuilder(getEmployeeMenuText(user));
        menu.append(NEW_LINE)
                .append("Сопровождение пользователей:").append(NEW_LINE)
                .append("- Открытые обращения: ").append(prepareShield(COMMAND_SHOW_OPEN_TASK)).append(NEW_LINE)
                .append("- Мои задачи: ").append(prepareShield(COMMAND_SHOW_MY_TASK)).append(NEW_LINE)
                .append("- Обновить справочники: ").append(prepareShield(COMMAND_RELOAD_DICTIONARY)).append(NEW_LINE);
        return menu.toString();
    }

    private String getAdminMenuText(User user) {
        val menu = new StringBuilder(getSupportMenuText(user));
        menu.append(NEW_LINE);
        menu.append("Меню Администратора:").append(NEW_LINE)
                .append("- Проверить новых пользователей: ").append(prepareShield(COMMAND_SETTING_NEW_USER)).append(NEW_LINE);
        return menu.toString();
    }

    private String prepareMainMenu() {
        val mainMenu = new StringBuilder();
        mainMenu.append("Главное меню:").append(NEW_LINE)
                .append("- часто задаваемые вопросы: ").append(COMMAND_FAQ).append(NEW_LINE)
                .append(NEW_LINE);
        return mainMenu.toString();
    }

    private String prepareAvailableConverters(Company company) {
        val menu = new StringBuilder();
        menu.append("Обработка файлов:").append(NEW_LINE);
        val converters = companySetting.getConverters(company);
        for (val convertService : converters) {
            var converterSettings = convertService.converterSettings();
            if (converterSettings != null && Boolean.FALSE.equals(converterSettings.getEnabled())) {
                log.info("Конвертер: " + convertService.getConverterName() + " выключен в конфигурации");
                continue;
            }
            menu.append("- ").append(convertService.getConverterName()).append(": ")
                    .append(SPACE).append(prepareShield(convertService.getConverterCommand())).append(NEW_LINE);
        }
        return menu.toString();
    }

    @Override
    public String getDescription() {
        return " Начало работы";
    }
}
