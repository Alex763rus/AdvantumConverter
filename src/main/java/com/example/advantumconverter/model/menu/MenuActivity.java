package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.jpa.User;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public interface MenuActivity {

    String getMenuComand();

    String getDescription();

    List<PartialBotApiMethod> menuRun(User user, Update update);

}
