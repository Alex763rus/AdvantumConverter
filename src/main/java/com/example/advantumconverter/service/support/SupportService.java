package com.example.advantumconverter.service.support;

import com.example.advantumconverter.model.jpa.SupportTask;
import com.example.advantumconverter.model.jpa.SupportTaskRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.service.excel.FileUploadService;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.vdurmont.emoji.EmojiParser;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.enums.Emoji.WARNING;
import static com.example.advantumconverter.enums.SupportTaskState.NEW;
import static com.example.advantumconverter.enums.UserRole.*;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.StringUtils.prepareShield;
import static org.example.tgcommons.utils.StringUtils.prepareTaskId;

@Slf4j
@Service
@AllArgsConstructor
public class SupportService {

    private final UserRepository userRepository;

    private final SupportTaskRepository supportTaskRepository;

    private final FileUploadService fileUploadService;

    private List<User> supportUserList;

    @PostConstruct
    public void init() {
        supportUserList = userRepository.findUserByUserRole(SUPPORT);
    }

    public List<PartialBotApiMethod> processNewTask(User user, Update update, ConvertService convertService
            , String fullFileName, Exception ex) throws ParseException {
        if (user.getUserRole() != EMPLOYEE && user.getUserRole() == MAIN_EMPLOYEE) {
            return SendMessageWrap.init().setChatIdLong(user.getChatId())
                    .setText(prepareShield(ex.getMessage()))
                    .build().createMessageList();
        }
        val message = update.getMessage();
        val inputFile = new InputFile(fileUploadService.uploadFileFromServer(fullFileName));

        val supportTask = new SupportTask();
        supportTask.setEmployeeChatId(user.getChatId());
        supportTask.setMessageId(message.getMessageId());
        supportTask.setConverterName(convertService.getConverterName());
        supportTask.setErrorText(ex.getMessage());
        supportTask.setFilePath(fullFileName);
        supportTask.setTaskState(NEW);
        supportTask.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        val supportEntity = supportTaskRepository.save(supportTask);

        val supportMessages = new ArrayList<PartialBotApiMethod>();
        val userErrorMessage = SendMessageWrap.init()
                .setChatIdLong(message.getChatId())
                .setText(getErrorMessageText(supportEntity.getSupportTaskId()))
                .build().createMessage();
        supportMessages.add(userErrorMessage);

        for (User supportUser : supportUserList) {
            supportMessages.add(
                    SendMessageWrap.init().setChatIdLong(supportUser.getChatId())
                            .setText(getSupportMessageText(supportEntity))
                            .build().createMessage());
            supportMessages.add(
                    SendDocumentWrap.init().setChatIdLong(supportUser.getChatId())
                            .setDocument(inputFile)
                            .build().createMessage());
        }
        return supportMessages;
    }

    private String getErrorMessageText(Long supportId) {
        StringBuilder errorMessageText = new StringBuilder();
        errorMessageText.append(EmojiParser.parseToUnicode(WARNING.getCode())).append("  Во время обработки файла произошла ошибка.").append(NEW_LINE)
                .append("Информация передана в поддержку, номер задачи: ").append(prepareTaskId(supportId)).append(NEW_LINE)
                .append("Пожалуйста, не пробуйте прогрузить этот файл повторно.").append(NEW_LINE)
                .append("Можете продолжать работать с другими файлами и конвертерами");
        return errorMessageText.toString();
    }

    private String getSupportMessageText(SupportTask supportTaskEntity) {
        StringBuilder supportMessageText = new StringBuilder();
        supportMessageText.append("Поступила новая заявка, номер: ").append(prepareTaskId(supportTaskEntity.getSupportTaskId())).append(NEW_LINE)
                .append("Текст ошибки:").append(supportTaskEntity.getErrorText()).append(NEW_LINE)
                .append("Используемый конвертер:").append(supportTaskEntity.getConverterName()).append(NEW_LINE)
                .append("Загружаемый файл переслан в сообщении ниже:");
        return supportMessageText.toString();
    }
}
