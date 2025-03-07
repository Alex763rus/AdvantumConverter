package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.booker.ConvertServiceImplBooker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_BOOKER;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_BOOKER;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_BOOKER;

@Component(COMMAND_CONVERT_BOOKER)
@AllArgsConstructor
@Slf4j
public class MenuConvertBooker extends MenuConverterBase {

    private final ConvertServiceImplBooker convertServiceImplBooker;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_BOOKER;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_BOOKER, FILE_NAME_BOOKER);
            case CONVERT_FILE_BOOKER -> convertFileLogic(user, update, convertServiceImplBooker);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_BOOKER);
    }

}
