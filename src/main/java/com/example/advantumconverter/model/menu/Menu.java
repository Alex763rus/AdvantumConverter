package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.database.UserService;
import com.example.advantumconverter.service.excel.ExcelGenerateService;
import com.example.advantumconverter.service.excel.FileUploadService;
import com.example.advantumconverter.service.menu.ButtonService;
import com.example.advantumconverter.service.menu.StateService;
import com.example.advantumconverter.service.support.SupportService;
import jakarta.persistence.MappedSuperclass;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;

@MappedSuperclass
public abstract class Menu implements MenuActivity {

    @Autowired
    protected BotConfig botConfig;

    @Autowired
    protected StateService stateService;

    @Autowired
    protected FileUploadService fileUploadService;

    @Autowired
    protected ExcelGenerateService excelGenerateService;

    @Autowired
    protected ButtonService buttonService;

    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected SupportService supportService;

    private static final String DEFAULT_TEXT_ERROR = "Ошибка! Команда не найдена";

    protected List<PartialBotApiMethod> errorMessageDefault(Update update) {
        return SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(DEFAULT_TEXT_ERROR)
                .build().createMessageList();
    }

    protected List<PartialBotApiMethod> errorMessage(Update update, String message) {
        return SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(message)
                .build().createMessageList();
    }

    protected List<PartialBotApiMethod> createErrorDefaultMessage(User user) {
        return createMessageList(user, DEFAULT_TEXT_ERROR);
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message) {
        return List.of(this.createMessage(user, message));
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message, ButtonsDescription buttonsDescription) {
        return List.of(this.createMessage(user, message, buttonsDescription));
    }

    protected PartialBotApiMethod createMessage(User user, String message) {
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText(message)
                .build().createMessage();
    }

    protected PartialBotApiMethod createMessage(User user, String message, ButtonsDescription buttonsDescription) {
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText(message)
                .setInlineKeyboardMarkup(createVerticalColumnMenu(buttonsDescription))
                .build().createMessage();
    }
}
