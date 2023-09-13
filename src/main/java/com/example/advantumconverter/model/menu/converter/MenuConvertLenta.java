package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.ConvertServiceImplLenta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_COFIX;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_LENTA;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_LENTA;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_LENTA;

@Component(COMMAND_CONVERT_LENTA)
@Slf4j
public class MenuConvertLenta extends MenuConverterBase {

    @Autowired
    protected ConvertServiceImplLenta convertServiceImplLenta;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_LENTA;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update, CONVERT_FILE_LENTA, FILE_NAME_LENTA);
            case CONVERT_FILE_LENTA:
                return convertFileLogic(user, update, convertServiceImplLenta);
        }
        return errorMessageDefault(update);
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_LENTA;
    }

}
