package com.example.advantumconverter.service.support;

import com.example.advantumconverter.model.jpa.SupportTask;
import com.example.advantumconverter.model.jpa.SupportTaskRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.model.wpapper.ForwardMessageWrap;
import com.example.advantumconverter.model.wpapper.SendMessageWrap;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.vdurmont.emoji.EmojiParser;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.NEW_LINE;
import static com.example.advantumconverter.enums.Emoji.WARNING;
import static com.example.advantumconverter.enums.SupportTaskState.NEW;
import static com.example.advantumconverter.enums.UserRole.SUPPORT;
import static com.example.advantumconverter.utils.StringUtils.prepareTaskId;

@Slf4j
@Service
public class SupportService {

    List<User> supportUserList;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SupportTaskRepository supportTaskRepository;

    @PostConstruct
    public void init() {
        supportUserList = userRepository.findUserByUserRole(SUPPORT);
    }

    public List<PartialBotApiMethod> processNewTask(User user, Update update, ConvertService convertService, Exception ex) {
        if (user.getUserRole() == SUPPORT) {
            return List.of(SendMessageWrap.init().setChatIdLong(user.getChatId())
                    .setText(ex.getMessage())
                    .build().createSendMessage());
        }
        val supportTask = new SupportTask();
        supportTask.setEmployeeChatId(user.getChatId());
        supportTask.setMessageId(update.getMessage().getMessageId());
        supportTask.setConverterName(convertService.getConverterName());
        supportTask.setErrorText(ex.getMessage());
        supportTask.setTaskState(NEW);
        supportTask.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        val supportEntity = supportTaskRepository.save(supportTask);

        val supportMessages = new ArrayList<PartialBotApiMethod>();
        val userErrorMessage = SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(getErrorMessageText(supportEntity.getSupportTaskId()))
                .build().createSendMessage();
        supportMessages.add(userErrorMessage);
        //сохранить файл на сервер
        //сохранить название файла в таске
        //отправить инфу в саппорт

        for (User supportUser : supportUserList) {
            supportMessages.add(
                    SendMessageWrap.init().setChatIdLong(supportUser.getChatId())
                            .setText(getSupportMessageText(supportEntity))
                            .build().createSendMessage());
            supportMessages.add(
                    ForwardMessageWrap.init().setChatIdLong(supportUser.getChatId())
                            .setMessageId(update.getMessage().getMessageId())
                            .setChatIdFromString(String.valueOf(update.getMessage().getChatId()))
                            .build().createMessage()
            );
        }
        return supportMessages;
    }

    private String getErrorMessageText(Long supportId) {
        val errorMessageText = new StringBuilder();
        errorMessageText.append(EmojiParser.parseToUnicode(WARNING.getCode())).append("  Во время обработки файла произошла ошибка.").append(NEW_LINE)
                .append("Информация передана в поддержку, номер задачи: ").append(prepareTaskId(supportId)).append(NEW_LINE)
                .append("Пожалуйста, не пробуйте прогрузить этот файл повторно.").append(NEW_LINE)
                .append("Можете продолжать работать с другими файлами и конвертерами");
        return errorMessageText.toString();
    }

    private String getSupportMessageText(SupportTask supportTaskEntity) {
        val supportMessageText = new StringBuilder();
        supportMessageText.append("Поступила новая заявка, номер: ").append(prepareTaskId(supportTaskEntity.getSupportTaskId())).append(NEW_LINE)
                .append("Текст ошибки:").append(supportTaskEntity.getErrorText()).append(NEW_LINE)
                .append("Используемый конвертер:").append(supportTaskEntity.getConverterName()).append(NEW_LINE)
                .append("Загружаемый файл переслан в сообщении ниже:");
        return supportMessageText.toString();
    }
}
