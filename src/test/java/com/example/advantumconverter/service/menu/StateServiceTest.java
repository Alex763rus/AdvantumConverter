package com.example.advantumconverter.service.menu;

import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.menu.Menu;
import com.example.advantumconverter.model.menu.MenuDefault;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StateServiceTest {

    private final StateService stateService = new StateService();

    private User user(long chatId) {
        var u = new User();
        u.setChatId(chatId);
        return u;
    }

    @Test
    void getState_defaultsToFree() {
        assertThat(stateService.getState(user(1L))).isEqualTo(State.FREE);
    }

    @Test
    void setState_andGet() {
        var u = user(1L);
        stateService.setState(u, State.CONVERT_FILE_LENTA);
        assertThat(stateService.getState(u)).isEqualTo(State.CONVERT_FILE_LENTA);
        assertThat(stateService.getUser(1L)).isSameAs(u);
    }

    @Test
    void menu_setGetDelete() {
        var u = user(1L);
        Menu menu = new MenuDefault();
        stateService.setMenu(u, menu);
        assertThat(stateService.getMenu(u)).isSameAs(menu);
        assertThat(stateService.getState(u)).isEqualTo(State.FREE);

        stateService.deleteUser(u);
        assertThat(stateService.getMenu(u)).isNull();
        assertThat(stateService.getUser(1L)).isNull();
    }

    @Test
    void deleteUser_nullSafe_forUnknownChat() {
        stateService.deleteUser(user(99L));
    }

    @Test
    void refreshUser_resetsState() {
        var u = user(1L);
        stateService.setMenu(u, new MenuDefault());
        stateService.setState(u, State.CONVERT_FILE_LENTA);

        stateService.refreshUser(u);

        assertThat(stateService.getState(u)).isEqualTo(State.FREE);
        assertThat(stateService.getUser(1L)).isSameAs(u);
    }
}
