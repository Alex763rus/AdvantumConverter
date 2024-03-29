package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplAgroprom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_AGROPROM;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_AGROPROM;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_AGROPROM;

@Component(COMMAND_CONVERT_AGROPROM)
@Slf4j
public class MenuConvertAgroprom extends MenuConverterBase {

    @Autowired
    protected ConvertServiceImplAgroprom convertServiceImplAgroprom;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_AGROPROM;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update, CONVERT_FILE_AGROPROM, FILE_NAME_AGROPROM);
            case CONVERT_FILE_AGROPROM:
                return convertFileLogic(user, update, convertServiceImplAgroprom);
        }
        return errorMessageDefault(update);
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_AGROPROM;
    }

}
