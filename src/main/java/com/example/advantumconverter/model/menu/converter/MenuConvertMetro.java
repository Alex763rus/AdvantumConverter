package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplMetro;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_METRO;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_METRO;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_METRO;

@Component(COMMAND_CONVERT_METRO)
@AllArgsConstructor
@Slf4j
public class MenuConvertMetro extends MenuConverterBase {

    private final ConvertServiceImplMetro convertServiceImplMetro;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_METRO;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_METRO, FILE_NAME_METRO);
            case CONVERT_FILE_METRO -> convertFileLogic(user, update, convertServiceImplMetro);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_METRO);
    }

}
