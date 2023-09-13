package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_DEFAULT;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Component
@Slf4j
public class MenuDefault extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_DEFAULT;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return createMessageList(user, "Не найдена доступная команда с именем: " + prepareShield(update.getMessage().getText()));
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
