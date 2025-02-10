package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.Menu;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.service.SecurityService;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.generate.ClientExcelGenerateService;
import com.example.advantumconverter.service.excel.generate.ExcelGenerateService;
import com.example.advantumconverter.service.rest.out.mapper.BookToCrmReisMapper;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.enums.FileType.USER_IN;
import static com.example.advantumconverter.enums.State.CONVERTER_WAIT_UNLOAD_IN_CRM;
import static com.example.advantumconverter.enums.State.FREE;
import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;

@MappedSuperclass
public abstract class MenuConverterBase extends Menu {

    @Autowired
    protected ClientExcelGenerateService excelGenerateService;

    @Autowired
    private Map<String, ExcelGenerateService> excelGenerateServiceMap;

    private Map<User, ConvertedBook> convertedBooks = new HashMap<>();
    private Map<User, ConvertedBookV2> convertedBooksV2 = new HashMap<>();

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

                    var answer = SendDocumentWrap.init()
                            .setChatIdLong(update.getMessage().getChatId())
                            .setCaption(message)
                            .setDocument(document)
                            .build();

                    if (SecurityService.grantApiUser(user)) { //todo проверить доступ
                        ArrayList<PartialBotApiMethod> answers = new ArrayList<>();
                        convertedBooks.put(user, convertedBook);
                        convertedBooksV2.put(user, convertedBookV2);

                        stateService.setState(user, FREE);
                        val buttons = new ArrayList<Button>();
                        buttons.add(Button.init().setKey(CONVERTER_WAIT_UNLOAD_IN_CRM.name()).setValue("Загрузить в CRM").build());
                        buttons.add(Button.init().setKey(FREE.name()).setValue("Главное меню").build());
                        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();

                        stateService.setState(user, CONVERTER_WAIT_UNLOAD_IN_CRM);

                        return List.of(
                                answer.createMessage(),
                                SendMessageWrap.init()
                                        .setChatIdLong(update.getMessage().getChatId())
                                        .setText("Выберите действие:")
                                        .setInlineKeyboardMarkup(createVerticalColumnMenu(buttonsDescription))
                                        .build().createMessage()
                        );
                    } else {
                        stateService.setState(user, FREE);
                    }
                    return answer.createMessageList();
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

    protected List<PartialBotApiMethod> unloadInCrmLogic(User user, Update update, ConvertService convertService) {
        if (update.hasCallbackQuery()) {
            val state = State.valueOf(update.getCallbackQuery().getData());
            return switch (state) {
                case FREE -> freeLogic(user, update);
                case CONVERTER_WAIT_UNLOAD_IN_CRM -> unloadInCrm(user, update, convertService.getCrmCreds());
                default -> errorMessageDefault(update);
            };
        }
        return errorMessageDefault(update);
    }

    protected List<PartialBotApiMethod> freeLogic(User user, Update update) {
        stateService.setState(user, FREE);
        convertedBooks.remove(user);
        convertedBooksV2.remove(user);
        val answer = new ArrayList<PartialBotApiMethod>();
        answer.add(createDeleteMessage(user.getChatId(), update.getCallbackQuery().getMessage().getMessageId()));
        answer.add(createMessage(user, "Выбрано меню: главное меню"));
        return answer;
    }

    protected List<PartialBotApiMethod> unloadInCrm(User user, Update update, CrmConfigProperties.CrmCreds crmCreds) {
        val convertedBookV1 = convertedBooks.get(user);
        val convertedBookV2 = convertedBooksV2.get(user);
        val crmGatewayResponseDto = convertedBookV1 != null
                ? crmHelper.sendDocument(BookToCrmReisMapper.map(convertedBookV1), crmCreds)
                : crmHelper.sendDocument(BookToCrmReisMapper.map(convertedBookV2), crmCreds);

        stateService.setState(user, FREE);
        convertedBooks.remove(user);
        convertedBooksV2.remove(user);
        val answer = new ArrayList<PartialBotApiMethod>();
        answer.add(createDeleteMessage(user.getChatId(), update.getCallbackQuery().getMessage().getMessageId()));
        answer.add(createMessage(user, "Выбрано меню: загрузить в CRM"));
        answer.add(createMessage(user, crmGatewayResponseDto.getMessage()));
        return answer;
    }
}
