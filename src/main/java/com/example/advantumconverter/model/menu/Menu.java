package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.exception.CarNotFoundException;
import com.example.advantumconverter.model.security.User;
import com.example.advantumconverter.model.wpapper.SendDocumentWrap;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import com.example.advantumconverter.service.FileUploadService;
import com.example.advantumconverter.service.excel.ConvertService;
import com.example.advantumconverter.service.excel.ExcelGenerateService;
import com.example.advantumconverter.service.menu.StateService;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

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

    protected List<PartialBotApiMethod> convertFileLogic(User user, Update update, ConvertService convertService) {
        if (update.hasMessage()) {
            if (update.getMessage().hasDocument()) {
                try {
                    val field = update.getMessage().getDocument();
                    val book = fileUploadService.uploadXlsx(field.getFileName(), field.getFileId());
                    val convertedBook = convertService.getConvertedBook(book);
                    val document = excelGenerateService.processXlsx(convertedBook, convertService.getFileNamePrefix(), SHEET_RESULT_NAME);
                    stateService.setState(user, FREE);
                    return Arrays.asList(SendDocumentWrap.init()
                            .setChatIdLong(update.getMessage().getChatId())
                            .setDocument(document)
                            .build().createMessage());
                } catch (Exception ex) {
                    return errorMessage(update, ex.getMessage());
                }
            } else{
                return errorMessage(update, "Ошибка. Сообщение не содержит документ.\nОтправьте документ");
            }
        }
        return errorMessageDefault(update);
    }
}
