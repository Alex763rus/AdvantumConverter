package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplArtFruit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_ART_FRUIT;
import static com.example.advantumconverter.enums.State.CONVERT_FILE_ART_FRUIT;

@Component(COMMAND_CONVERT_ART_FRUIT)
@Slf4j
public class MenuConvertArtFruit extends MenuConverterBase {

    @Autowired
    protected ConvertServiceImplArtFruit convertServiceImplArtFruit;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_ART_FRUIT;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return switch (stateService.getState(user)) {
            case FREE -> freeLogic(user, update, CONVERT_FILE_ART_FRUIT, FILE_NAME_ART_FRUIT);
            case CONVERT_FILE_ART_FRUIT -> convertFileLogic(user, update, convertServiceImplArtFruit);
            case CONVERTER_WAIT_UNLOAD_IN_CRM -> unloadInCrmLogic(user, update, convertServiceImplArtFruit);
            default -> errorMessageDefault(update);
        };
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_ART_FRUIT;
    }

}
