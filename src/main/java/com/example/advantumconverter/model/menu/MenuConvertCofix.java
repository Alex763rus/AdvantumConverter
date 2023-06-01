package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import com.example.advantumconverter.service.excel.converter.ConvertServiceImplCofix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_COFIX;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_COFIX;

@Component
@Slf4j
public class MenuConvertCofix extends Menu {

    @Autowired
    protected ConvertServiceImplCofix convertServiceImplCofix;

    @Override
    public String getMenuComand() {
        return COMMAND_CONVERT_COFIX;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freeLogic(user, update);
            case CONVERT_FILE_COFIX:
                return convertFileLogic(user, update, convertServiceImplCofix);
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> freeLogic(User user, Update update) {
        if(!update.getMessage().getText().equals(getMenuComand())){
            return errorMessageDefault(update);
        }
        stateService.setState(user, State.CONVERT_FILE_COFIX);
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText("Отправьте исходный файл " + FILE_NAME_COFIX + ":")
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return "Сконвертировать файл " + FILE_NAME_COFIX;
    }

}
