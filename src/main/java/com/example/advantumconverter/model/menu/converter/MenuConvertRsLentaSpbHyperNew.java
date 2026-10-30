package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.rs.ConvertServiceImplRsLentaSpbHyperNew;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_LENTA_SPB_HYPER_NEW;
import static com.example.advantumconverter.constant.Constant.Converter.CONVERT_FILE_DESCRIPTION_TEMPLATE;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_LENTA_SPB_HYPER_NEW;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_RS_LENTA_SPB_HYPER_NEW;

@Component(COMMAND_CONVERT_RS_LENTA_SPB_HYPER_NEW)
@AllArgsConstructor
@Slf4j
public class MenuConvertRsLentaSpbHyperNew extends MenuConverterBase {

    private final ConvertServiceImplRsLentaSpbHyperNew convertServiceImplRsLentaSpbHyperNew;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_RS_LENTA_SPB_HYPER_NEW;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_RS_LENTA_SPB_HYPER_NEW, FILE_NAME_RS_LENTA_SPB_HYPER_NEW);
            case CONVERT_FILE_RS_LENTA_SPB_HYPER_NEW ->
                    convertFileLogic(user, update, convertServiceImplRsLentaSpbHyperNew);
            case CONVERTER_WAIT_UNLOAD_IN_CRM -> unloadInCrmLogic(user, update, convertServiceImplRsLentaSpbHyperNew);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return String.format(CONVERT_FILE_DESCRIPTION_TEMPLATE, FILE_NAME_RS_LENTA_SPB_HYPER_NEW);
    }

}
