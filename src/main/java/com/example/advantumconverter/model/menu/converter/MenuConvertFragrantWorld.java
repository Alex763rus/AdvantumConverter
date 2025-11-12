package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplFragrantWorld;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_FRAGRANT_WORLD;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_FRAGRANT_WORLD;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_FRAGRANT_WORLD;

@Component(COMMAND_CONVERT_FRAGRANT_WORLD)
@AllArgsConstructor
@Slf4j
public class MenuConvertFragrantWorld extends MenuConverterBase {

    private final ConvertServiceImplFragrantWorld convertServiceImplFragrantWorld;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_FRAGRANT_WORLD;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_FRAGRANT_WORLD, FILE_NAME_FRAGRANT_WORLD);
            case CONVERT_FILE_FRAGRANT_WORLD -> convertFileLogic(user, update, convertServiceImplFragrantWorld);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_FRAGRANT_WORLD);
    }

}
