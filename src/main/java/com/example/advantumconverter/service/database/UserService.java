package com.example.advantumconverter.service.database;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.service.menu.StateService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;

import static com.example.advantumconverter.enums.UserRole.NEED_SETTING;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StateService stateService;

    @Autowired
    private CompanyRepository companyRepository;

    public User getUser(Update update) {
        val message = getMessage(update);
        val chatId = message.getChatId();
        User user = stateService.getUser(chatId);
        if (user == null) {
            val users = userRepository.findById(chatId);
            user = users.isPresent() ? users.get() : registeredUser(message);
        }
        return user;
    }

    private Message getMessage(Update update) {
        if (update.hasMessage()) {
            return update.getMessage();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage();
        }
        return null;
    }

    private User registeredUser(Message message) {
        val chatId = message.getChatId();
        val chat = message.getChat();

        User user = new User();

        user.setChatId(chatId);
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setUserName(chat.getUserName());
        user.setUserRole(NEED_SETTING);
        user.setCompany(companyRepository.getCompaniesByCompanyName("Компания отсутствует"));
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
        log.info("user saved: " + user);
        return user;
    }
}
