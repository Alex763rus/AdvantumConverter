package com.example.advantumconverter.api.bot;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.service.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DistributionServiceTest {

    private final TelegramBot telegramBot = org.mockito.Mockito.mock(TelegramBot.class);
    private final BotConfig botConfig = org.mockito.Mockito.mock(BotConfig.class);
    private final DistributionService service = new DistributionService();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "telegramBot", telegramBot);
        ReflectionTestUtils.setField(service, "botConfig", botConfig);
    }

    @Test
    void init_success() throws Exception {
        org.mockito.Mockito.when(botConfig.getAdminChatId()).thenReturn("1");
        org.mockito.Mockito.when(botConfig.getBotVersion()).thenReturn("1.0");
        org.mockito.Mockito.doReturn(null).when(telegramBot).execute(org.mockito.ArgumentMatchers.any(org.telegram.telegrambots.meta.api.methods.BotApiMethod.class));
        service.init();
    }

    @Test
    void init_exception() {
        org.mockito.Mockito.when(botConfig.getAdminChatId()).thenThrow(new RuntimeException("boom"));
        service.init();
    }

    @Test
    void squeezyExit_and_sendToAdmin() throws Exception {
        org.mockito.Mockito.when(botConfig.getAdminChatId()).thenReturn("42");
        org.mockito.Mockito.when(botConfig.getBotVersion()).thenReturn("2.0");
        org.mockito.Mockito.doReturn(null).when(telegramBot).execute(org.mockito.ArgumentMatchers.any(org.telegram.telegrambots.meta.api.methods.BotApiMethod.class));
        service.squeezyExit();
        service.sendTgMessageToAdmin("hi");
    }

    @Test
    void sendMessage_telegramException() throws Exception {
        org.mockito.Mockito.when(botConfig.getAdminChatId()).thenReturn("42");
        org.mockito.Mockito.doThrow(new org.telegram.telegrambots.meta.exceptions.TelegramApiException("fail"))
                .when(telegramBot).execute(org.mockito.ArgumentMatchers.any(org.telegram.telegrambots.meta.api.methods.BotApiMethod.class));
        service.squeezyExit();
    }
}
