package com.example.advantumconverter.model.menu.support;

import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.database.DictionaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuReloadDictionaryTest {

    @Mock
    private DictionaryService dictionaryService;

    @InjectMocks
    private MenuReloadDictionary menu;

    @Test
    void comandAndDescription() {
        assertThat(menu.getMenuComand()).isEqualTo("/reload_dictionary");
        assertThat(menu.getDescription()).isEqualTo("/reload_dictionary");
    }

    @Test
    void menuRun_reloads() {
        var user = new User();
        user.setChatId(1L);
        assertThat(menu.menuRun(user, new Update())).isNotEmpty();
        verify(dictionaryService).reloadDictionary();
    }
}
