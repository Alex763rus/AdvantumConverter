package com.example.advantumconverter.model.menu.support;

import com.example.advantumconverter.model.jpa.SupportTask;
import com.example.advantumconverter.model.jpa.SupportTaskRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.Menu;
import com.example.advantumconverter.utils.DateConverter;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.ParseException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.NEW_LINE;
import static com.example.advantumconverter.enums.State.FREE;
import static com.example.advantumconverter.enums.State.SUPPORT_WAIT_CHOOSE_TASK;
import static com.example.advantumconverter.utils.StringUtils.prepareShield;
import static com.example.advantumconverter.utils.StringUtils.prepareTaskId;

@MappedSuperclass
public abstract class MenuTaskBase extends Menu {


    @Autowired
    protected SupportTaskRepository supportTaskRepository;
    protected Map<User, SupportTask> userTmp = new HashMap();

    protected String getTaskInfo(SupportTask supportTask) throws ParseException {
        val taskInfo = new StringBuilder();
        taskInfo.append("Задача ").append(supportTask.getSupportTaskId()).append(NEW_LINE)
                .append("Конвертер: ").append(supportTask.getConverterName()).append(NEW_LINE)
                .append("Зарегистрирована: ").append(DateConverter.convertDateFormat(supportTask.getRegisteredAt(), DateConverter.TEMPLATE_DATE_TIME_DOT)).append(NEW_LINE)
                .append("Текст ошибки: ").append(supportTask.getErrorText()).append(NEW_LINE)
        ;
        return taskInfo.toString();
    }

    protected List<PartialBotApiMethod> createMenuFromUserTasks(User user, Update update, List<SupportTask> supportTasks, String mainMessage, String notFoundTask) {
        if (supportTasks.size() == 0) {
            stateService.setState(user, FREE);
            return SendMessageWrap.init()
                    .setChatIdLong(user.getChatId())
                    .setText(notFoundTask)
                    .build().createMessageList();
        }
        val btns = new LinkedHashMap<String, String>();
        for (int i = 0; i < supportTasks.size(); ++i) {
            btns.put(String.valueOf(supportTasks.get(i).getSupportTaskId()), prepareShield(String.valueOf(prepareTaskId(supportTasks.get(i).getSupportTaskId()))));
        }
        stateService.setState(user, SUPPORT_WAIT_CHOOSE_TASK);
        return SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText(mainMessage)
                        .setInlineKeyboardMarkup(buttonService.createVerticalMenu(btns))
                        .build().createMessageList();
    }

}
