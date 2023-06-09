package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.menu.MenuActivity;
import com.example.advantumconverter.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.example.advantumconverter.enums.State.FREE;

@Slf4j
@Service
public class StateService {

    private Map<User, State> userState = new HashMap<>();
    private Map<User, MenuActivity> userMenu = new HashMap<>();

    public void setState(User user, State state) {
        userState.put(user, state);
    }

    public State getState(User user) {
        if (!userState.containsKey(user)) {
            userState.put(user, FREE);
        }
        return userState.get(user);
    }

    public MenuActivity getMenu(User user) {
        return userMenu.getOrDefault(user, null);
    }

    public User getUser(Long chatId) {
        User user = userState.entrySet().stream()
                .filter(entry -> (long) entry.getKey().getChatId() == (chatId))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
        return user;
    }

    public void setMenu(User user, MenuActivity mainMenu) {
        userMenu.put(user, mainMenu);
        userState.put(user, FREE);
    }
    public void deleteUser(User user) {
        userMenu.remove(user);
        userState.remove(user);
    }
    public void clearOldState() {
        userState.entrySet().removeIf(e -> e.getValue() == FREE);
    }

    public void refreshUser(User user){
        deleteUser(getUser(user.getChatId()));
        setState(user, FREE);
    }

}
