package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplSiel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SIEL;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SIEL;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_SIEL;

@Component(COMMAND_CONVERT_SIEL)
@AllArgsConstructor
@Slf4j
public class MenuConvertSiel extends MenuConverterBase {

    private final ConvertServiceImplSiel convertServiceImplSiel;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_SIEL;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_SIEL, FILE_NAME_SIEL);
            case CONVERT_FILE_SIEL -> convertFileLogic(user, update, convertServiceImplSiel);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_SIEL);
    }

}
