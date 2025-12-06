package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.rs.ConvertServiceImplRsLentaFish;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_LENTA_FISH;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_LENTA_FISH;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_RS_LENTA_FISH;

@Component(COMMAND_CONVERT_RS_LENTA_FISH)
@AllArgsConstructor
@Slf4j
public class MenuConvertRsLentaFish extends MenuConverterBase {

    private final ConvertServiceImplRsLentaFish convertServiceImplRsLentaFish;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_RS_LENTA_FISH;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_RS_LENTA_FISH, FILE_NAME_RS_LENTA_FISH);
            case CONVERT_FILE_RS_LENTA_FISH -> convertFileLogic(user, update, convertServiceImplRsLentaFish);
            case CONVERTER_WAIT_UNLOAD_IN_CRM -> unloadInCrmLogic(user, update, convertServiceImplRsLentaFish);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_RS_LENTA_FISH);
    }

}
