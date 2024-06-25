package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.jpa.FaqRepository;
import com.example.advantumconverter.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_FAQ;
import static com.example.advantumconverter.enums.State.FAQ_WAIT_QUESTION;
import static com.example.advantumconverter.enums.State.FREE;

@Component(COMMAND_FAQ)
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
                .build().createMessage());
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
        if (faq.isEmpty()) {
            return createMessageList(user, "Отсутствуют данные для faq, обратитесь к администратору");
        }
        val buttons = new ArrayList<Button>();
        for (com.example.advantumconverter.model.jpa.Faq value : faq) {
            buttons.add(Button.init().setKey(String.valueOf(value.getFaqId()))
                    .setValue(value.getQuestion()).build());
        }
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        stateService.setState(user, FAQ_WAIT_QUESTION);
        return createMessageList(user, "Выберете интересующий вопрос:", buttonsDescription);
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
