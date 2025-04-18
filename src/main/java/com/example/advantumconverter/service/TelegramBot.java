package com.example.advantumconverter.service;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.service.menu.MenuService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final MenuService menuService;

    private static final int MESSAGE_LEN_LIMIT = 4000;

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
                    if (answer instanceof SendMessage) {
                        val splitAnswers = splitAnswerOnToLongText((SendMessage) answer);
                        for (BotApiMethod ans : splitAnswers) {
                            execute(ans);
                        }
                    } else {
                        execute((BotApiMethod) answer);
                    }
                }
                if (answer instanceof SendDocument) {
                    execute((SendDocument) answer);
                }
            } catch (TelegramApiException e) {
                log.error("Ошибка во время обработки сообщения: " + e.getMessage());
            }
        }
    }


    private List<SendMessage> splitAnswerOnToLongText(SendMessage answer) {
        if (answer.getText() == null || answer.getText().length() < MESSAGE_LEN_LIMIT) {
            return List.of(answer);
        }
        val splitAnswerOnToLongTextList = new ArrayList<SendMessage>();
        val text = answer.getText();
        val tokens = new ArrayList<String>();

        int start = 0;
        int finish;
        while (start < text.length()) {
            finish = text.lastIndexOf(",", start + MESSAGE_LEN_LIMIT);
            if (start == finish) {
                finish = start + MESSAGE_LEN_LIMIT;
            }
            tokens.add(text.substring(start, Math.min(text.length(), finish)));
            start = finish;
        }
        for (val token : tokens) {
            splitAnswerOnToLongTextList.add(SendMessageWrap.init()
                    .setChatIdString(answer.getChatId())
                    .setText(token)
                    .build().createMessage()
            );
        }
        return splitAnswerOnToLongTextList;
    }

}
