package com.example.advantumconverter.model.wpapper;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.val;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static com.example.advantumconverter.constant.Constant.PARSE_MODE;

@Getter
@SuperBuilder(setterPrefix = "set", builderMethodName = "init", toBuilder = true)
public class ForwardMessageWrap {

    private String chatIdString;
    private Long chatIdLong;
    private String chatIdFromString;
    private Long chatIdFromLong;
    private Integer messageId;

    public ForwardMessage createMessage() {
        val forwardMessage = new ForwardMessage();
        val chatId = chatIdString == null ? String.valueOf(chatIdLong) : chatIdString;
        val chatIdFrom = chatIdFromString == null ? String.valueOf(chatIdFromLong) : chatIdFromString;
        forwardMessage.setChatId(chatId);
        forwardMessage.setFromChatId(chatIdFrom);
        forwardMessage.setMessageId(messageId);
        return forwardMessage;
    }
}
