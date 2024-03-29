package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.Menu;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Converter.SHEET_RESULT_NAME;
import static com.example.advantumconverter.enums.FileType.USER_IN;
import static com.example.advantumconverter.enums.State.FREE;

@MappedSuperclass
public abstract class MenuConverterBase extends Menu {

    protected List<PartialBotApiMethod> convertFileLogic(User user, Update update, ConvertService convertService) {
        if (update.hasMessage()) {
            if (update.getMessage().hasDocument()) {
                try {
                    val field = update.getMessage().getDocument();
                    val fileFullPath = fileUploadService.getFileName(USER_IN, field.getFileName());
                    update.getMessage().setText(fileFullPath);
                    val book = fileUploadService.uploadXlsx(fileFullPath, field.getFileId());

                    val convertedBook = convertService.getConvertedBook(book);
                    val document = excelGenerateService.processXlsx(convertedBook, convertService.getFileNamePrefix(), SHEET_RESULT_NAME);
                    stateService.setState(user, FREE);
                    return SendDocumentWrap.init()
                            .setChatIdLong(update.getMessage().getChatId())
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
