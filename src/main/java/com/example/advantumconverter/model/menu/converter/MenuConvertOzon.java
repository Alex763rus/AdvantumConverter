package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.ConvertServiceImplOzon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_OZON;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_OZON;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_OZON;

@Component(COMMAND_CONVERT_OZON)
@Slf4j
public class MenuConvertOzon extends MenuConverterBase {

    @Autowired
    protected ConvertServiceImplOzon convertServiceImplOzon;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_OZON;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_OZON, FILE_NAME_OZON);
            case CONVERT_FILE_OZON -> convertFileLogic(user, update, convertServiceImplOzon);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_OZON;
    }

}
