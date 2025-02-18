package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplBogorodsk;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_BOGORODSK;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_BOGORODSK;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_BOGORODSK;

@Component(COMMAND_CONVERT_BOGORODSK)
@AllArgsConstructor
@Slf4j
public class MenuConvertBogorodsk extends MenuConverterBase {

    private final ConvertServiceImplBogorodsk convertServiceImplBogorodsk;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_BOGORODSK;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_BOGORODSK, FILE_NAME_BOGORODSK);
            case CONVERT_FILE_BOGORODSK -> convertFileLogic(user, update, convertServiceImplBogorodsk);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_BOGORODSK);
    }

}
