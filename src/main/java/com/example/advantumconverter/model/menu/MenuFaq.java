package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.jpa.FaqRepository;
import com.example.advantumconverter.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_FAQ;
import static com.example.advantumconverter.constant.Constant.NEW_LINE;
import static com.example.advantumconverter.enums.State.*;

@Component
@Slf4j
public class MenuFaq extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_FAQ;
    }

    @Autowired
    private FaqRepository faqRepository;

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            switch (stateService.getState(user)) {
                case FREE:
                    return freelogic(user, update);
                case FAQ_WAIT_QUESTION:
                    return faqWaitQuestionLogic(user, update);
            }
            return errorMessageDefault(update);
        } catch (Exception ex) {
            log.error(ex.toString());
            return errorMessage(update, ex.toString());
        }
    }

    private List<PartialBotApiMethod> faqWaitQuestionLogic(User user, Update update) throws ParseException, IOException {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        val faq = faqRepository.findById(Long.parseLong(update.getCallbackQuery().getData())).get();
        val answer = new ArrayList<PartialBotApiMethod>();
        val filePath = faq.getFilePath();
        answer.add(SendMessageWrap.init()
                .setChatIdLong(update.getCallbackQuery().getMessage().getChatId())
                .setText("Ответ: " + faq.getAnswer())
                .build().createSendMessage());
        if (filePath != null) {
            answer.add(SendDocumentWrap.init()
                    .setChatIdLong(user.getChatId())
                    .setDocument(new InputFile(new File(filePath)))
                    .build().createMessage());
        }
        stateService.setState(user, FREE);
        return answer;
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val faq = faqRepository.findAll();
        if (faq.size() == 0) {
            return Arrays.asList(SendMessageWrap.init()
                    .setChatIdLong(user.getChatId())
                    .setText("Отсутствуют данные для faq, обратитесь к администратору")
                    .build().createSendMessage());
        }
        val btns = new LinkedHashMap<String, String>();
        for (int i = 0; i < faq.size(); ++i) {
            btns.put(String.valueOf(faq.get(i).getFaqId()), faq.get(i).getQuestion());
        }
        stateService.setState(user, FAQ_WAIT_QUESTION);
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText("Выберете интересующий вопрос:")
                .setInlineKeyboardMarkup(buttonService.createVerticalMenu(btns))
                .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
