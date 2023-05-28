package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.security.User;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import com.example.advantumconverter.service.excel.ConvertServiceImplLenta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.LENTA;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_LENTA;

@Component
@Slf4j
public class MenuConvertLenta extends Menu {

    final String MENU_NAME = "/convert_lenta";

    @Autowired
    protected ConvertServiceImplLenta convertServiceImplLenta;

    @Override
    public String getMenuName() {
        return MENU_NAME;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update);
            case CONVERT_FILE_LENTA:
                return convertFileLogic(user, update, convertServiceImplLenta);
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> freeLogic(User user, Update update) {
        if (!update.getMessage().getText().equals(MENU_NAME)) {
            return errorMessageDefault(update);
        }
        stateService.setState(user, CONVERT_FILE_LENTA);
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText("Отправьте исходный файл " + LENTA + ":")
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + LENTA;
    }

}
