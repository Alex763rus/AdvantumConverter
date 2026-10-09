package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.model.jpa.User;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.assertThat;

class MenuDefaultTest {

    @Test
    void comandAndDescription() {
        var menu = new MenuDefault();
        assertThat(menu.getMenuComand()).isEqualTo("/default");
        assertThat(menu.getDescription()).isEqualTo("/default");
    }

    @Test
    void menuRun_returnsMessage() {
        var user = new User();
        user.setChatId(1L);
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setText("/unknown");
        var update = new Update();
        update.setMessage(message);

        assertThat(new MenuDefault().menuRun(user, update)).isNotEmpty();
    }

    @Test
    void createErrorDefaultMessage_returnsMessage() {
        var user = new User();
        user.setChatId(1L);
        var result = (java.util.List<?>) org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(new MenuDefault(), "createErrorDefaultMessage", user);
        assertThat(result).isNotEmpty();
    }
}
