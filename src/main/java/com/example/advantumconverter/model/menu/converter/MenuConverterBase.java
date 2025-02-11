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
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileWriter;
import java.nio.file.Files;
import java.util.*;

import static com.example.advantumconverter.enums.FileType.USER_IN;
import static com.example.advantumconverter.enums.State.CONVERTER_WAIT_UNLOAD_IN_CRM;
import static com.example.advantumconverter.enums.State.FREE;
import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;

@MappedSuperclass
@Log4j2
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
                    var isV2 = convertedBook == null;
                    val document = isV2
                            ? excelService.createXlsxV2(convertedBookV2)
                            : excelService.createXlsx(convertedBook);
                    val message = isV2 ? convertedBookV2.getMessage() : convertedBook.getMessage();

                    var answer = SendDocumentWrap.init()
                            .setChatIdLong(update.getMessage().getChatId())
                            .setCaption(message)
                            .setDocument(document)
                            .build();

                    if (SecurityService.grantApiUser(user)
                            && isV2/* только для api v2*/) {
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
        if (Objects.requireNonNullElse(crmGatewayResponseDto.getReisError(), new ArrayList<>()).isEmpty()) {
            answer.add(createMessage(user, crmGatewayResponseDto.getMessage()));
        } else {
            try {
                val tmpFile = Files.createTempFile("log", ".txt").toFile();
                try (FileWriter writer = new FileWriter(tmpFile)) {
                    writer.write(crmGatewayResponseDto.getMessage());
                    System.out.println("Запись завершена.");
                }
                answer.add(SendDocumentWrap.init()
                        .setChatIdLong(update.getCallbackQuery().getMessage().getChatId())
                        .setCaption("Ошибка во время загрузки рейсов. Детализация во вложении")
                        .setDocument(new InputFile(tmpFile))
                        .build().createMessage());
            } catch (Exception ex) {
                log.error("не смогли приложить лог: " + crmGatewayResponseDto.getMessage());
            }
        }
        return answer;
    }
}
