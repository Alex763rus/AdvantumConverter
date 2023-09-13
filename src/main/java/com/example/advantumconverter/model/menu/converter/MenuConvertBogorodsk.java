package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.ConvertServiceImplBogorodsk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_AGROPROM_DETAIL;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_BOGORODSK;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_BOGORODSK;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_BOGORODSK;

@Component(COMMAND_CONVERT_BOGORODSK)
@Slf4j
public class MenuConvertBogorodsk extends MenuConverterBase {

    @Autowired
    protected ConvertServiceImplBogorodsk convertServiceImplBogorodsk;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_BOGORODSK;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update, CONVERT_FILE_BOGORODSK, FILE_NAME_BOGORODSK);
            case CONVERT_FILE_BOGORODSK:
                return convertFileLogic(user, update, convertServiceImplBogorodsk);
        }
        return errorMessageDefault(update);
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_BOGORODSK;
    }

}
