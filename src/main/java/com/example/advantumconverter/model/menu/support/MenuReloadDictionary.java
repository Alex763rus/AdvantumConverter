package com.example.advantumconverter.model.menu.support;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.Menu;
import com.example.advantumconverter.service.database.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_RELOAD_DICTIONARY;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_SHOW_MY_TASK;

@Component
@Slf4j
public class MenuReloadDictionary extends Menu {

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public String getMenuComand() {
        return COMMAND_RELOAD_DICTIONARY;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        dictionaryService.reloadDictionary();
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText("Справочники успешно обновлены!")
                .build().createMessageList();
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
