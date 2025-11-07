package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.rs.ConvertServiceImplRsLenta;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_LENTA;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_LENTA;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_LENTA;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_RS_LENTA;

@Component(COMMAND_CONVERT_RS_LENTA)
@AllArgsConstructor
@Slf4j
public class MenuConvertRsLenta extends MenuConverterBase {

    private final ConvertServiceImplRsLenta convertServiceImplRsLenta;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_RS_LENTA;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_RS_LENTA, FILE_NAME_RS_LENTA);
            case CONVERT_FILE_RS_LENTA -> convertFileLogic(user, update, convertServiceImplRsLenta);
            case CONVERTER_WAIT_UNLOAD_IN_CRM -> unloadInCrmLogic(user, update, convertServiceImplRsLenta);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_RS_LENTA);
    }

}
