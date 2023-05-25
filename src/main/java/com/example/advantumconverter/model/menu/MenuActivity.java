package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.security.User;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public interface MenuActivity {

    public String getMenuName();

    public String getDescription();

    public List<PartialBotApiMethod> menuRun(User user, Update update);

}