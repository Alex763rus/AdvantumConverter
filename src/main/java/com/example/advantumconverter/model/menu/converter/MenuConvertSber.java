package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplSber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SBER;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SBER;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_SBER;

@Component(COMMAND_CONVERT_SBER)
@Slf4j
public class MenuConvertSber extends MenuConverterBase {

    @Autowired
    protected ConvertServiceImplSber convertServiceImplSber;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_SBER;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_SBER, FILE_NAME_SBER);
            case CONVERT_FILE_SBER -> convertFileLogic(user, update, convertServiceImplSber);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_SBER;
    }

}
