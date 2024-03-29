package com.example.advantumconverter.model.menu.support;

import com.example.advantumconverter.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_SHOW_OPEN_TASK;
import static com.example.advantumconverter.enums.State.*;
import static com.example.advantumconverter.enums.SupportTaskState.IN_PROGRESS;
import static com.example.advantumconverter.enums.SupportTaskState.NEW;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

@Component(COMMAND_SHOW_OPEN_TASK)
@Slf4j
public class MenuOpenTask extends MenuTaskBase {

    @Override
    public String getMenuComand() {
        return COMMAND_SHOW_OPEN_TASK;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            switch (stateService.getState(user)) {
                case FREE:
                    return freelogic(user, update);
                case SUPPORT_WAIT_CHOOSE_TASK:
                    return supportWaitChooseTaskLogic(user, update);
            }
        } catch (Exception ex) {
            log.error(ex.toString());
            return errorMessage(update, ex.toString());
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> supportWaitChooseTaskLogic(User user, Update update) throws ParseException {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val supportTask = supportTaskRepository.findById(Long.parseLong(update.getCallbackQuery().getData())).get();
        if (supportTask.getSupportChatId() != null) {
            //значит задачу уже взял другой сопровожденец
            val supportsList = new ArrayList<PartialBotApiMethod>();
            supportsList.add(SendMessageWrap.init()
                    .setChatIdLong(update.getCallbackQuery().getMessage().getChatId())
                    .setText("Ошибка: Выбранную задачу уже взяли в работу." + NEW_LINE
                            + "Пожалуйста, выберите другую задачу:")
                    .build().createMessage());
            supportsList.addAll(freelogic(user, update));
            return supportsList;
        }
        supportTask.setSupportChatId(user.getChatId());
        supportTask.setTaskState(IN_PROGRESS);
        supportTaskRepository.save(supportTask);
        stateService.setState(user, FREE);
        val inputFile = new InputFile(fileUploadService.uploadFileFromServer(supportTask.getFilePath()));

        val resultText = new StringBuilder(getTaskInfo(supportTask));
        resultText.append(NEW_LINE).append("Для обработки задачи перейдите в меню \"мои задачи\"");

        return List.of(SendMessageWrap.init().setChatIdLong(user.getChatId())
                        .setText(resultText.toString())
                        .build().createMessage(),
                SendDocumentWrap.init().setChatIdLong(user.getChatId())
                        .setDocument(inputFile)
                        .build().createMessage());
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val supportsTask = supportTaskRepository.findByTaskState(NEW);
        val mainMessage = "Взять открытое обращение в работу:";
        val notFoundMessage = "Открытые обращения: отсутствуют";
        return createMenuFromUserTasks(user, update, supportsTask, mainMessage, notFoundMessage);
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
