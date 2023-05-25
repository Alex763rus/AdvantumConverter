package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.menu.MenuActivity;
import com.example.advantumconverter.model.security.User;
import com.example.advantumconverter.model.security.WhiteListUser;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StateService {

    @Autowired
    WhiteListUser whiteListUser;
    private Map<User, State> userState = new HashMap<>();
    private Map<User, MenuActivity> userMenu = new HashMap<>();

    @PostConstruct
    public void init() {
        whiteListUser.getUsers().stream().forEach(e -> userState.put(e, State.FREE));
    }

    public void setState(User user, State state) {
        userState.put(user, state);
    }

    public State getState(User user) {
        if (!userState.containsKey(user)) {
            userState.put(user, State.FREE);
        }
        return userState.get(user);
    }

    public MenuActivity getMenu(User user) {
        return userMenu.getOrDefault(user, null);
    }

    public User getUser(String chatId) {
        return userState.entrySet().stream()
                .filter(entry -> entry.getKey().getChatId().equals(chatId))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }

    public void setMenu(User user, MenuActivity mainMenu) {
        userMenu.put(user, mainMenu);
        userState.put(user, State.FREE);
    }

    public void clearOldState() {
        userState.entrySet().removeIf(e -> e.getValue() == State.FREE);
    }


}
