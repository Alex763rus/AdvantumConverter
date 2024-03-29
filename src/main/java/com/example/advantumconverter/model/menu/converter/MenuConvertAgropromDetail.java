package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplAgropromDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_AGROPROM_DETAIL;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_AGROPROM_DETAIL;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_AGROPROM_DETAIL;

@Component(COMMAND_CONVERT_AGROPROM_DETAIL)
@Slf4j
public class MenuConvertAgropromDetail extends MenuConverterBase {

    @Autowired
    protected ConvertServiceImplAgropromDetail convertServiceImplAgropromDetail;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_AGROPROM_DETAIL;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update, CONVERT_FILE_AGROPROM_DETAIL, FILE_NAME_AGROPROM_DETAIL);
            case CONVERT_FILE_AGROPROM_DETAIL:
                return convertFileLogic(user, update, convertServiceImplAgropromDetail);
        }
        return errorMessageDefault(update);
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_AGROPROM_DETAIL;
    }

}
