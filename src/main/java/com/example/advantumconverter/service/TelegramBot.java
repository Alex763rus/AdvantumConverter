package com.example.advantumconverter.service;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.service.excel.ConvertService;
import com.example.advantumconverter.service.menu.MenuService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ConvertService convertService;

    @PostConstruct
    public void init() {
        try {
            execute(new SetMyCommands(menuService.getMainMenuComands(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
        log.info("==" + "Server was starded. Version: " + botConfig.getBotVersion() + "==================================================================================================");
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Autowired
    FileUploadService fileUploadService;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            if(update.getMessage().hasDocument()){
                val field = update.getMessage().getDocument();
                try {
                    val file = fileUploadService.uploadFile(field.getFileName(), field.getFileId());
                    val book = (XSSFWorkbook) WorkbookFactory.create(file);
                    val document = convertService.process(book);

                    val sendDocument = new SendDocument();
                    sendDocument.setDocument(document);
                    sendDocument.setChatId(String.valueOf(update.getMessage().getChatId()));
                    execute(sendDocument);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        val answers = menuService.messageProcess(update);
        for (val answer : answers) {
            try {
                if (answer instanceof BotApiMethod) {
                    execute((BotApiMethod) answer);
                }
            } catch (TelegramApiException e) {
                log.error("Ошибка во время обработки сообщения: " + e.getMessage());
            }
        }
    }

}
