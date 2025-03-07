package com.example.advantumconverter.service;

import com.example.advantumconverter.model.jpa.HistoryAction;
import com.example.advantumconverter.model.jpa.HistoryActionRepository;
import com.example.advantumconverter.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.List;

import static com.example.advantumconverter.enums.HistoryActionType.SYSTEM_ACTION;
import static com.example.advantumconverter.enums.HistoryActionType.USER_ACTION;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@Service
public class HistoryActionService {

    private static final int MAX_MESSAGE_LENGTH = 999;

    private String prepareMessageText(String messageText) {
        if(messageText == null){
            return EMPTY;
        }
        return messageText.length() > MAX_MESSAGE_LENGTH ?
                messageText.substring(0, MAX_MESSAGE_LENGTH) :
                messageText;
    }

    @Autowired
    private HistoryActionRepository historyActionRepository;

    public void saveHistoryAction(User user, Update update) {
        val historyAction = new HistoryAction();
        historyAction.setActionDate(new Timestamp(System.currentTimeMillis()));
        historyAction.setChatIdFrom(user.getChatId());
        historyAction.setActionType(USER_ACTION);
        if (update.hasMessage()) {
            val message = update.getMessage();
            if (message.hasText()) {
                if (message.getText().contains("Главное")) {
                    return;
                }
                historyAction.setMessageText(prepareMessageText(message.getText()));
            }
            if (message.hasDocument()) {
                historyAction.setMessageText(prepareMessageText(message.getCaption()));
                historyAction.setFileName(update.getMessage().getText());
            }
        }
        if (update.hasCallbackQuery()) {
            val menuName = update.getCallbackQuery().getMessage().getReplyMarkup().getKeyboard().stream()
                    .filter(e -> e.get(0).getCallbackData().equals(update.getCallbackQuery().getData()))
                    .findFirst().get().get(0).getText();
            historyAction.setCallbackMenuName(menuName);
        }
        historyActionRepository.save(historyAction);
    }

    public void saveHistoryAnswerAction(User user, List<PartialBotApiMethod> answers) {
        for (PartialBotApiMethod answer : answers) {
            saveHistoryAnswerAction(user, answer);
        }
    }

    private void saveHistoryAnswerAction(User user, PartialBotApiMethod partialBotApiMethod) {
        val historyAction = new HistoryAction();
        historyAction.setActionDate(new Timestamp(System.currentTimeMillis()));
        historyAction.setChatIdFrom(user.getChatId());
        historyAction.setActionType(SYSTEM_ACTION);
        if (partialBotApiMethod instanceof SendMessage) {
            val answer = (SendMessage) partialBotApiMethod;
            if (answer.getText().contains("Главное")) {
                return;
            }
            historyAction.setMessageText(prepareMessageText(answer.getText()));
            historyAction.setChatIdTo(Long.parseLong(answer.getChatId()));
        }
        if (partialBotApiMethod instanceof EditMessageText) {
            val answer = (EditMessageText) partialBotApiMethod;
            if (answer.getText().contains("Главное")) {
                return;
            }
            historyAction.setMessageText(prepareMessageText(answer.getText()));
            historyAction.setChatIdTo(Long.parseLong(answer.getChatId()));
        }
        if (partialBotApiMethod instanceof SendDocument) {
            val answer = (SendDocument) partialBotApiMethod;
            historyAction.setMessageText(prepareMessageText(answer.getCaption()));
            historyAction.setChatIdTo(Long.parseLong(answer.getChatId()));
            if (answer.getDocument() != null) {
                historyAction.setFileName(answer.getDocument().getAttachName());
            }
        }
        historyActionRepository.save(historyAction);
    }
}
