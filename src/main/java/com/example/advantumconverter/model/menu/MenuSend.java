package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.security.User;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.SPACE;

@Component
@Slf4j
public class MenuSend extends Menu {

    final String MENU_NAME = "/send";

    @Override
    public String getMenuName() {
        return MENU_NAME;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
//        val client = senderSettings.getClientList().stream()
//                .filter(e -> e.getChatIdOwner().equals(user.getChatId()))
//                .findFirst().get();
//        client.setEnabled(true);
        val messageText ="Рассылка запущена";
        log.info(messageText + SPACE + user.getChatId());
        return Arrays.asList(
                createAdminMessage(messageText + ": "+ user.getChatId()),
                SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText(messageText)
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return "Включить рассылку";
    }

}
