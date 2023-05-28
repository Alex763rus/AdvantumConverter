package com.example.advantumconverter.service;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.service.excel.ConvertServiceBase;
import com.example.advantumconverter.service.menu.MenuService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ConvertServiceBase convertServiceBase;

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

    @Override
    public void onUpdateReceived(Update update) {
        val answers = menuService.messageProcess(update);
        for (val answer : answers) {
            try {
                if (answer instanceof BotApiMethod) {
                    execute((BotApiMethod) answer);
                }
                if (answer instanceof SendDocument) {
                    //deleteLastMessage(update);
                    execute((SendDocument) answer);
                }
            } catch (TelegramApiException e) {
                log.error("Ошибка во время обработки сообщения: " + e.getMessage());
            }
        }
    }

    private void deleteLastMessage(Update update) throws TelegramApiException {
        EditMessageText editMessageText = new EditMessageText();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId((int) messageId);
        editMessageText.setText("Документ готов!");
        execute(editMessageText);
    }

}
