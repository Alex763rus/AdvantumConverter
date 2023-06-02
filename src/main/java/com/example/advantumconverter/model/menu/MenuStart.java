package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.*;
import static com.example.advantumconverter.constant.Constant.NEW_LINE;
import static com.example.advantumconverter.constant.Constant.SPACE;
import static com.example.advantumconverter.enums.Emoji.BLUSH;
import static com.example.advantumconverter.utils.StringUtils.getShield;

@Component
@Slf4j
public class MenuStart extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_START;
    }

    @Autowired
    private CompanySetting companySetting;

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        String messageText = "";
        switch (user.getUserRole()) {
            case NEED_SETTING:
                log.warn("ожидает обработки новый пользователь ChatId:" + update.getMessage().getChatId());
                messageText = "Добрый день, " + getShield(user.getUserName()) + "! " + BLUSH.getCode() + "\n\nВы успешно зарегистрированы в системе.\nОжидайте, после настройки вашего аккаунта вам придет сообщение";
                break;
            case BLOCKED:
                messageText = "Доступ запрещен";
            case EMPLOYEE:
                messageText = getEmployeeMenuText(user);
                break;
            case MAIN_EMPLOYEE:
                messageText = getMainEmployeeMenuText(user);
                break;
            case SUPPORT:
                messageText = getSupportMenuText(user);
                break;
            case ADMIN:
                messageText = getAdminMenuText(user);
                break;

        }
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(user.getChatId())
                        .setText(EmojiParser.parseToUnicode(messageText))
                        .build().createSendMessage());
    }

    private String getMainEmployeeMenuText(User user) {
        val menu = new StringBuilder();
        menu.append("- справочная информация: ").append(COMMAND_FAQ).append(NEW_LINE)
                .append(NEW_LINE)
                .append("Обработка файлов:").append(NEW_LINE);
        val converters = companySetting.getConverters(user.getCompany());
        for (val convertService : converters) {
            menu.append("- ").append(convertService.getConverterName()).append(": ")
                    .append(SPACE).append(getShield(convertService.getConverterCommand())).append(NEW_LINE);
        }
        return menu.toString();
    }

    private String getSupportMenuText(User user) {
        val menu = new StringBuilder();
        menu.append("- справочная информация: ").append(COMMAND_FAQ).append(NEW_LINE)
                .append(NEW_LINE)
                .append("Обработка файлов:").append(NEW_LINE);
        val converters = companySetting.getConverters(user.getCompany());
        for (val convertService : converters) {
            menu.append("- ").append(convertService.getConverterName()).append(": ")
                    .append(SPACE).append(getShield(convertService.getConverterCommand())).append(NEW_LINE);
        }
        return menu.toString();
    }

    private String getAdminMenuText(User user) {
        val menu = new StringBuilder();
        menu.append("Проверить новых пользователей: ").append(NEW_LINE).append(getShield(COMMAND_SETTING_NEW_USER)).append(NEW_LINE);
        return menu.toString();
    }

    private String getEmployeeMenuText(User user) {
        val menu = new StringBuilder();
        menu.append("- справочная информация: ").append(COMMAND_FAQ).append(NEW_LINE)
                .append(NEW_LINE)
                .append("Обработка файлов:").append(NEW_LINE);
        val converters = companySetting.getConverters(user.getCompany());
        for (val convertService : converters) {
            menu.append("- ").append(convertService.getConverterName()).append(": ")
                    .append(SPACE).append(getShield(convertService.getConverterCommand())).append(NEW_LINE);
        }
        return menu.toString();
    }

    @Override
    public String getDescription() {
        return " Начало работы";
    }
}
