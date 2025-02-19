package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplDominos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_DOMINOS;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_DOMINOS;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_DOMINOS;

@Component(COMMAND_CONVERT_DOMINOS)
@AllArgsConstructor
@Slf4j
public class MenuConvertDominos extends MenuConverterBase {

    private final ConvertServiceImplDominos convertServiceImplDominos;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_DOMINOS;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update, CONVERT_FILE_DOMINOS, FILE_NAME_DOMINOS);
            case CONVERT_FILE_DOMINOS:
                return convertFileLogic(user, update, convertServiceImplDominos);
        }
        return errorMessageDefault(update);
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_DOMINOS);
    }

}
