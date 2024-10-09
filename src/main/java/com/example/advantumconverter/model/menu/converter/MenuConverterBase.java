package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.Menu;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.generate.ClientExcelGenerateService;
import com.example.advantumconverter.service.excel.generate.ExcelGenerateService;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.enums.FileType.USER_IN;
import static com.example.advantumconverter.enums.State.FREE;

@MappedSuperclass
public abstract class MenuConverterBase extends Menu {

    @Autowired
    protected ClientExcelGenerateService excelGenerateService;

    @Autowired
    private Map<String, ExcelGenerateService> excelGenerateServiceMap;

    protected List<PartialBotApiMethod> convertFileLogic(User user, Update update, ConvertService convertService) {
        if (update.hasMessage()) {
            if (update.getMessage().hasDocument()) {
                try {
                    val field = update.getMessage().getDocument();
                    if (!field.getFileName().contains(".xlsx")
                            && !field.getFileName().contains(".xlsm")) {
                        return errorMessage(update, "Ошибка. Неизвестный формат файла. \nОтправьте документ формата .xlsx");
                    }
                    val fileFullPath = fileUploadService.getFileName(USER_IN, field.getFileName());
                    update.getMessage().setText(fileFullPath);
                    val book = fileUploadService.uploadXlsx(fileFullPath, field.getFileId());
                    var convertedBook = convertService.getConvertedBook(book);
                    var convertedBookV2 = convertService.getConvertedBookV2(book);
                    val excelService = excelGenerateServiceMap.get(convertService.getExcelType().getExcelType());
                    val document = convertedBook != null
                            ? excelService.createXlsx(convertedBook)
                            : excelService.createXlsxV2(convertedBookV2);
                    val message = convertedBook != null ? convertedBook.getMessage() : convertedBookV2.getMessage();
                    stateService.setState(user, FREE);
                    return SendDocumentWrap.init()
                            .setChatIdLong(update.getMessage().getChatId())
                            .setCaption(message)
                            .setDocument(document)
                            .build().createMessageList();
                } catch (Exception ex) {
                    try {
                        return supportService.processNewTask(user, update, convertService, update.getMessage().getText(), ex);
                    } catch (Exception e) {
                        //todo
                    }
                }
            } else {
                return errorMessage(update, "Ошибка. Сообщение не содержит документ.\nОтправьте документ");
            }
        }
        return errorMessageDefault(update);
    }

    protected List<PartialBotApiMethod> freeLogic(User user, Update update, State state, String fileName) {
        if (!update.getMessage().getText().equals(getMenuComand())) {
            return errorMessageDefault(update);
        }
        stateService.setState(user, state);
        return SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText("Отправьте исходный файл " + fileName + ":")
                .build().createMessageList();
    }
}
