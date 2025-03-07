package com.example.advantumconverter.service.database;

import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.service.menu.StateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Objects;

import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NOT_FOUND;
import static com.example.advantumconverter.enums.UserRole.NEED_SETTING;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final StateService stateService;

    private final CompanyRepository companyRepository;

    public User getUser(Update update) {
        val message = getMessage(update);
        val chatId = message.getChatId();
        return Objects.requireNonNullElse(
                stateService.getUser(chatId),
                userRepository.findById(chatId).orElseGet(() -> registerNewUser(message))
        );
    }

    public void refreshUser(User user) {
        stateService.refreshUser(user);
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

    private User registerNewUser(Message message) {
        val chatId = message.getChatId();
        val chat = message.getChat();

        User user = new User();

        user.setChatId(chatId);
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setUserName(chat.getUserName());
        user.setUserRole(NEED_SETTING);
        user.setUserRoletext(NEED_SETTING.name());
        user.setCompany(companyRepository.getCompaniesByCompanyName(COMPANY_NOT_FOUND));

        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
        log.info("user saved: " + user);
        return user;
    }
}
