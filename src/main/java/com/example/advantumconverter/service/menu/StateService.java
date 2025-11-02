package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.MenuActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.example.advantumconverter.enums.State.FREE;

@Slf4j
@Service
public class StateService {

    private final Map<User, State> userState = new HashMap<>();
    private final Map<User, MenuActivity> userMenu = new HashMap<>();

    public void setState(User user, State state) {
        userState.put(user, state);
    }

    public State getState(User user) {
        return userState.computeIfAbsent(user, u -> FREE);
    }

    public MenuActivity getMenu(User user) {
        return userMenu.getOrDefault(user, null);
    }

    public User getUser(Long chatId) {
        return userState.keySet().stream()
                .filter(user -> chatId.equals(user.getChatId()))
                .findAny()
                .orElse(null);
    }

    public void setMenu(User user, MenuActivity mainMenu) {
        userMenu.put(user, mainMenu);
        userState.put(user, FREE);
    }

    public void deleteUser(User user) {
        userMenu.remove(user);
        userState.remove(user);
    }

    public void refreshUser(User user) {
        deleteUser(getUser(user.getChatId()));
        setState(user, FREE);
    }

}
