package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import com.example.advantumconverter.service.excel.converter.ConvertServiceImplDominos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_DOMINOS;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_DOMINOS;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_DOMINOS;

@Component
@Slf4j
public class MenuConvertDominos extends Menu {

    @Autowired
    protected ConvertServiceImplDominos convertServiceImplDominos;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_DOMINOS;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update);
            case CONVERT_FILE_DOMINOS:
                return convertFileLogic(user, update, convertServiceImplDominos);
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> freeLogic(User user, Update update) {
        if(!update.getMessage().getText().equals(getMenuComand())){
            return errorMessageDefault(update);
        }
        stateService.setState(user, CONVERT_FILE_DOMINOS);
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText("Отправьте исходный файл " + FILE_NAME_DOMINOS + ":")
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_DOMINOS;
    }

}
