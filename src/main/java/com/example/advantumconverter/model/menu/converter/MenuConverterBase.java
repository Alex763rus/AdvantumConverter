package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.aspect.LogExecutionTime;
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
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@MappedSuperclass
@Log4j2
public abstract class MenuConverterBase extends Menu {

    private static final int CAPTION_LEN_LIMIT = 800;

    @Autowired
    protected ClientExcelGenerateService excelGenerateService;

    @Autowired
    private Map<String, ExcelGenerateService> excelGenerateServiceMap;

    private Map<User, ConvertedBook> convertedBooks = new HashMap<>();
    private Map<User, ConvertedBookV2> convertedBooksV2 = new HashMap<>();

    @LogExecutionTime(value = "Полная обработка файла", unit = LogExecutionTime.TimeUnit.SECONDS)
    protected List<PartialBotApiMethod> convertFileLogic(User user, Update update, ConvertService convertService) {
        if (!update.hasMessage()) {
            return errorMessageDefault(update);
        }
        var updateMessage = update.getMessage();
        if (!updateMessage.hasDocument()) {
            return errorMessage(update, "Ошибка. Сообщение не содержит документ.\nОтправьте документ");
        }
        try {
            val field = updateMessage.getDocument();
            if (!field.getFileName().contains(".xlsx")
                    && !field.getFileName().contains(".xlsm")) {
                return errorMessage(update, "Ошибка. Неизвестный формат файла. \nОтправьте документ формата .xlsx");
            }
            val fileFullPath = fileUploadService.getFileName(USER_IN, field.getFileName());
            log.info(String.format("Приложен файл: %s, пользователь: %s, конвертер: %s",
                    fileFullPath, user.getChatId(), convertService.getConverterName()));
            updateMessage.setText(fileFullPath);
            val book = fileUploadService.uploadXlsx(fileFullPath, field.getFileId());
            var convertedBook = convertService.getConvertedBook(book);
            var convertedBookV2 = convertService.getConvertedBookV2(book);
            val excelService = excelGenerateServiceMap.get(convertService.getExcelType().getExcelType());
            var isV2 = convertedBook == null;
            val document = isV2
                    ? excelService.createXlsxV2(convertedBookV2)
                    : excelService.createXlsx(convertedBook);
            val message = isV2 ? convertedBookV2.getMessage() : convertedBook.getMessage();

            String caption;
            if (message != null && message.length() > CAPTION_LEN_LIMIT) {
                caption = message.substring(0, CAPTION_LEN_LIMIT) + NEW_LINE + NEW_LINE + "Весь текст не уместился .....";
            } else {
                caption = message;
            }
            var answer = SendDocumentWrap.init()
                    .setChatIdLong(updateMessage.getChatId())
                    .setCaption(caption)
                    .setDocument(document)
                    .build();

            log.info(String.format("Файл успешно обработан: %s, пользователь: %s, конвертер: %s",
                    fileFullPath, user.getChatId(), convertService.getConverterName()));
            if (SecurityService.grantApiUser(user)
                    && isV2/* только для api v2*/
                    && convertService.getCrmCreds() != null /* креды заполнены */
            ) {
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
                                .setChatIdLong(updateMessage.getChatId())
                                .setText("Выберите действие:")
                                .setInlineKeyboardMarkup(createVerticalColumnMenu(buttonsDescription))
                                .build().createMessage()
                );
            } else {
                stateService.setState(user, FREE);
            }
            return answer.createMessageList();
        } catch (Exception ex) {
            log.error(String.format("Во время обработки файла пользователь: %s, конвертер: %s возникла ошибка: %s",
                    user.getChatId(), convertService.getConverterName(), ex.getMessage()));
            return SendMessageWrap.init().setChatIdLong(user.getChatId())
                    .setText(prepareShield(ex.getMessage()))
                    .build().createMessageList();
        }
    }

    protected List<PartialBotApiMethod> freeLogic(User user, Update update, State state, String fileName) {
        //TODO NPE: update.getMessage()
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

    @LogExecutionTime(value = "Загрузка в CRM", unit = LogExecutionTime.TimeUnit.SECONDS)
    protected List<PartialBotApiMethod> unloadInCrm(User user, Update update, CrmConfigProperties.CrmCreds crmCreds) {
        val convertedBookV1 = convertedBooks.get(user);
        val convertedBookV2 = convertedBooksV2.get(user);

        var bookName = Objects.isNull(convertedBookV1) ?
                Objects.isNull(convertedBookV2) ? "имя файла неизвестно" : convertedBookV2.getBookName() :
                convertedBookV1.getBookName();

        log.info(String.format("Пользователь в CRM загружает файл: %s, пользователь: %s", bookName, user.getChatId()));
        val crmGatewayResponseDto = convertedBookV1 != null
                ? crmHelper.sendDocument(BookToCrmReisMapper.map(convertedBookV1), crmCreds)
                : crmHelper.sendDocument(BookToCrmReisMapper.map(convertedBookV2), crmCreds);

        stateService.setState(user, FREE);
        convertedBooks.remove(user);
        convertedBooksV2.remove(user);
        val answer = new ArrayList<PartialBotApiMethod>();
        answer.add(createDeleteMessage(user.getChatId(), update.getCallbackQuery().getMessage().getMessageId()));
        answer.add(createMessage(user, "Выбрано меню: загрузить в АТМС"));
        if (Objects.requireNonNullElse(crmGatewayResponseDto.getReisError(), new ArrayList<>()).isEmpty()) {
            answer.add(createMessage(user, crmGatewayResponseDto.getMessage()));
        } else {
            try {
                log.info("Начало формирования лога с ошибками обработки файла, пользователь: " + user.getChatId());
                val tmpFile = Files.createTempFile("log", ".txt").toFile();
                try (FileWriter writer = new FileWriter(tmpFile)) {
                    writer.write(crmGatewayResponseDto.getMessage());
                    log.info("Запись завершена");
                }
                answer.add(SendDocumentWrap.init()
                        .setChatIdLong(update.getCallbackQuery().getMessage().getChatId())
                        .setCaption("Ошибка во время загрузки рейсов. Детализация во вложении")
                        .setDocument(new InputFile(tmpFile))
                        .build().createMessage());
                log.info("Файл с ошибками обработки файла успешно сформирован и отправлен, пользователь: " + user.getChatId());
            } catch (Exception ex) {
                log.error("не смогли приложить лог: " + crmGatewayResponseDto.getMessage());
            }
        }
        return answer;
    }
}
