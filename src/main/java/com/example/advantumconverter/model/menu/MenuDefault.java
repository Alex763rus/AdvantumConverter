package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.security.User;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.utils.StringUtils.getShield;

@Component
@Slf4j
public class MenuDefault extends Menu {

    final String MENU_NAME = "/default";

    @Override
    public String getMenuName() {
        return MENU_NAME;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText("^_^ Not a found command with name: " + getShield(update.getMessage().getText()))
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return MENU_NAME;
    }

}
