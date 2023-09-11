package com.example.advantumconverter.model.menu.support;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_SHOW_MY_TASK;
import static com.example.advantumconverter.enums.FileType.SUPPORT_IN;
import static com.example.advantumconverter.enums.State.*;
import static com.example.advantumconverter.enums.SupportTaskState.DONE;
import static com.example.advantumconverter.enums.SupportTaskState.IN_PROGRESS;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.StringUtils.prepareTaskId;

@Component
@Slf4j
public class MenuMyTask extends MenuTaskBase {

    @Override
    public String getMenuComand() {
        return COMMAND_SHOW_MY_TASK;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            switch (stateService.getState(user)) {
                case FREE:
                    return freelogic(user, update);
                case SUPPORT_WAIT_CHOOSE_TASK:
                    return supportWaitChooseTaskLogic(user, update);
                case SUPPORT_WAIT_MODE_WORK:
                    return waitModeWorkLogic(user, update);
                case SUPPORT_WAIT_RESOLVE_MESSAGE:
                    return waitResolveMesasgeLogic(user, update);
            }
        } catch (Exception ex) {
            log.error(ex.toString());
            return errorMessage(update, ex.toString());
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> waitResolveMesasgeLogic(User user, Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasDocument()) {
                try {
                    val task = userTmp.get(user);
//                    val book = fileUploadService.uploadFileFromServer(task.getFilePath());
                    val field = update.getMessage().getDocument();
                    val fileFullPath = fileUploadService.getFileName(SUPPORT_IN, field.getFileName());
                    update.getMessage().setText(fileFullPath);
                    val book = fileUploadService.uploadFileFromTg(fileFullPath, field.getFileId());

                    val resultText = new StringBuilder();
                    task.setResultText(update.getMessage().getCaption());
                    task.setTaskState(DONE);
                    task.setCloseAt(new Timestamp(System.currentTimeMillis()));
                    resultText.append("Сообщение от поддержки:").append(NEW_LINE)
                            .append("Задача " + prepareTaskId(task.getSupportTaskId()) + " выполнена").append(NEW_LINE)
                            .append("Причина возникновения ошибки:").append(task.getResultText()).append(NEW_LINE)
                            .append("Корректный файл во вложении ниже, попробуйте его сконвертировать:")
                    ;
                    supportTaskRepository.save(task);
                    userTmp.remove(user);
                    stateService.setState(user, FREE);
                    return Arrays.asList(
                            SendMessageWrap.init()
                                    .setChatIdLong(task.getEmployeeChatId())
                                    .setText(resultText.toString())
                                    .build().createMessage(),
                            SendDocumentWrap.init()
                                    .setChatIdLong(task.getEmployeeChatId())
                                    .setDocument(new InputFile(book))
                                    .build().createMessage(),
                            SendMessageWrap.init()
                                    .setChatIdLong(user.getChatId())
                                    .setText("Задача " + prepareTaskId(task.getSupportTaskId()) + " успешно закрыта!")
                                    .build().createMessage()
                    );
                } catch (Exception ex) {
                    return errorMessage(update, "Ошибка во время формирования ответа пользователю:" + ex.getMessage());
                }
            } else {
                return errorMessage(update, "Ошибка. Сообщение не содержит документ.\nОтправьте документ");
            }
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> waitModeWorkLogic(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val state = Enum.valueOf(State.class, update.getCallbackQuery().getData());
        switch (state) {
            case CANCEL:
                return cancelLogic(user, update);
            case SUPPORT_WAIT_RESOLVE_TASK:
                return taskInProgresLogic(user, update);
        }
        return new ArrayList<>();
    }

    private List<PartialBotApiMethod> cancelLogic(User user, Update update) {
        stateService.setState(user, FREE);
        userTmp.remove(user);
        return new ArrayList<>();
    }

    private List<PartialBotApiMethod> taskInProgresLogic(User user, Update update) {
        val resultText = new StringBuilder();
        val task = userTmp.get(user);
        stateService.setState(user, SUPPORT_WAIT_RESOLVE_MESSAGE);
        resultText.append("Для решения задачи: ").append(prepareTaskId(task.getSupportTaskId())).append(NEW_LINE)
                .append("- приложите корректный файл").append(NEW_LINE)
                .append("- напишите краткое описание причин возникновения ошибки");
        return SendMessageWrap.init().setChatIdLong(user.getChatId())
                .setText(resultText.toString())
                .build().createMessageList();
    }


    private List<PartialBotApiMethod> supportWaitChooseTaskLogic(User user, Update update) throws ParseException {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val task = supportTaskRepository.findById(Long.parseLong(update.getCallbackQuery().getData())).get();
        userTmp.put(user, task);
        val inputFile = new InputFile(fileUploadService.uploadFileFromServer(task.getFilePath()));
        val btns = new LinkedHashMap<String, String>();
        btns.put(SUPPORT_WAIT_RESOLVE_TASK.name(), "Выполнить");
        btns.put(CANCEL.name(), "Отмена");
        stateService.setState(user, SUPPORT_WAIT_MODE_WORK);
        return List.of(SendMessageWrap.init().setChatIdLong(user.getChatId())
                        .setText(getTaskInfo(task))
                        .build().createMessage(),
                SendDocumentWrap.init().setChatIdLong(user.getChatId())
                        .setDocument(inputFile)
                        .build().createMessage(),
                SendMessageWrap.init()
                        .setChatIdLong(user.getChatId())
                        .setText("Выполнить задачу:")
                        .setInlineKeyboardMarkup(buttonService.createVerticalMenu(btns))
                        .build().createMessage()
        );
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val mySupportsTask = supportTaskRepository.findBySupportChatIdAndTaskState(user.getChatId(), IN_PROGRESS);
        val mainMessage = "Мои задачи в работе:";
        val notFoundMessage = "Мои задачи в работе: отсутствуют";
        return createMenuFromUserTasks(user, update, mySupportsTask, mainMessage, notFoundMessage);
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
