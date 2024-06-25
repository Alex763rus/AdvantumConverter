package com.example.advantumconverter.model.menu.support;

import com.example.advantumconverter.model.jpa.SupportTask;
import com.example.advantumconverter.model.jpa.SupportTaskRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.Menu;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.enums.State.FREE;
import static com.example.advantumconverter.enums.State.SUPPORT_WAIT_CHOOSE_TASK;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.DateConverterUtils.TEMPLATE_DATE_TIME_DOT;
import static org.example.tgcommons.utils.DateConverterUtils.convertDateFormat;
import static org.example.tgcommons.utils.StringUtils.prepareShield;
import static org.example.tgcommons.utils.StringUtils.prepareTaskId;

@MappedSuperclass
public abstract class MenuTaskBase extends Menu {


    @Autowired
    protected SupportTaskRepository supportTaskRepository;
    protected Map<User, SupportTask> userTmp = new HashMap();

    protected String getTaskInfo(SupportTask supportTask) throws ParseException {
        val taskInfo = new StringBuilder();
        taskInfo.append("Задача ").append(supportTask.getSupportTaskId()).append(NEW_LINE)
                .append("Конвертер: ").append(supportTask.getConverterName()).append(NEW_LINE)
                .append("Зарегистрирована: ").append(convertDateFormat(supportTask.getRegisteredAt(), TEMPLATE_DATE_TIME_DOT)).append(NEW_LINE)
                .append("Текст ошибки: ").append(supportTask.getErrorText()).append(NEW_LINE)
        ;
        return taskInfo.toString();
    }

    protected List<PartialBotApiMethod> createMenuFromUserTasks(User user, Update update, List<SupportTask> supportTasks, String mainMessage, String notFoundTask) {
        if (supportTasks.isEmpty()) {
            stateService.setState(user, FREE);
            return createMessageList(user, notFoundTask);
        }
        val buttons = new ArrayList<Button>();
        for (SupportTask supportTask : supportTasks) {
            buttons.add(Button.init().setKey(String.valueOf(supportTask.getSupportTaskId()))
                    .setValue(prepareShield(String.valueOf(prepareTaskId(supportTask.getSupportTaskId())))).build());
        }
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        stateService.setState(user, SUPPORT_WAIT_CHOOSE_TASK);
        return createMessageList(user, mainMessage, buttonsDescription);
    }

}
