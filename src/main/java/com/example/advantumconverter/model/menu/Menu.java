package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.wpapper.SendDocumentWrap;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import com.example.advantumconverter.service.database.UserService;
import com.example.advantumconverter.service.excel.FileUploadService;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.ExcelGenerateService;
import com.example.advantumconverter.service.menu.ButtonService;
import com.example.advantumconverter.service.menu.StateService;
import com.example.advantumconverter.service.support.SupportService;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.NEW_LINE;
import static com.example.advantumconverter.constant.Constant.SHEET_RESULT_NAME;
import static com.example.advantumconverter.enums.State.FREE;

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
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(DEFAULT_TEXT_ERROR)
                .build().createSendMessage());
    }

    protected List<PartialBotApiMethod> errorMessage(Update update, String message) {
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(message)
                .build().createSendMessage());
    }

    protected PartialBotApiMethod createAdminMessage(String message) {
        return SendMessageWrap.init()
                .setChatIdString(botConfig.getAdminChatId())
                .setText(message)
                .build().createSendMessage();
    }


}
