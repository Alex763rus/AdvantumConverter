package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.converter.MenuConverterBase;
import com.example.advantumconverter.service.menu.StateService;
import com.example.advantumconverter.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuConverterSubclassesIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private List<MenuConverterBase> menus;

    @Autowired
    private StateService stateService;

    @Test
    void allConverterMenus_freeState() {
        assertThat(menus).isNotEmpty();
        long chatId = 900001L;
        for (MenuConverterBase menu : menus) {
            var user = new User();
            user.setChatId(chatId++);
            user.setUserRole(UserRole.EMPLOYEE);
            stateService.setState(user, com.example.advantumconverter.enums.State.FREE);

            var message = new Message();
            message.setChat(new Chat(user.getChatId(), "private"));
            message.setText(menu.getMenuComand());
            var update = new Update();
            update.setMessage(message);

            assertThat(menu.getMenuComand()).isNotBlank();
            assertThat(menu.getDescription()).isNotNull();
            assertThat(menu.menuRun(user, update)).isNotEmpty();

            stateService.deleteUser(user);
        }
    }

    private Update messageUpdate(long chatId, String text) {
        var message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(text);
        var update = new Update();
        update.setMessage(message);
        return update;
    }

    @Test
    void allConverterMenus_convertState_withoutDocument() {
        assertThat(menus).isNotEmpty();
        long chatId = 910001L;
        for (MenuConverterBase menu : menus) {
            var user = new User();
            user.setChatId(chatId++);
            user.setUserRole(UserRole.EMPLOYEE);

            var cmd = menu.getMenuComand();
            var suffix = cmd.startsWith("/convert_") ? cmd.substring("/convert_".length()) : cmd.substring(1);
            var state = com.example.advantumconverter.enums.State.valueOf("CONVERT_FILE_" + suffix.toUpperCase());
            stateService.setState(user, state);

            assertThat(menu.menuRun(user, messageUpdate(user.getChatId(), cmd))).isNotEmpty();

            stateService.deleteUser(user);
        }
    }

    @Test
    void allConverterMenus_unloadState_and_default() {
        assertThat(menus).isNotEmpty();
        long chatId = 920001L;
        for (MenuConverterBase menu : menus) {
            var user = new User();
            user.setChatId(chatId++);
            user.setUserRole(UserRole.EMPLOYEE);

            stateService.setState(user, com.example.advantumconverter.enums.State.CONVERTER_WAIT_UNLOAD_IN_CRM);
            assertThat(menu.menuRun(user, messageUpdate(user.getChatId(), "x"))).isNotEmpty();

            stateService.setState(user, com.example.advantumconverter.enums.State.CANCEL);
            assertThat(menu.menuRun(user, messageUpdate(user.getChatId(), "x"))).isNotEmpty();

            stateService.deleteUser(user);
        }
    }
}
