package com.example.advantumconverter.support;

import com.example.advantumconverter.config.BotInitializer;
import com.example.advantumconverter.config.DatabaseTestConfig;
import com.example.advantumconverter.service.TelegramBot;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.TimeUnit;

/**
 * Base class for full-context integration tests.
 * <p>
 * - PostgreSQL runs in a Testcontainers container (shared across the JVM).
 * - Telegram is replaced by a Mockito mock.
 * - Outbound REST calls to the external CRM are pointed at a local MockWebServer,
 *   so the real WebClient code paths are exercised.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    protected static MockWebServer crmMockServer;

    @MockBean
    protected TelegramBot telegramBot;

    @MockBean
    protected BotInitializer botInitializer;

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        DatabaseTestConfig.start();
        if (crmMockServer == null) {
            MockWebServer server = new MockWebServer();
            try {
                server.start();
            } catch (Exception e) {
                throw new IllegalStateException("Не удалось запустить MockWebServer", e);
            }
            crmMockServer = server;
        }
        registry.add("spring.datasource.url", DatabaseTestConfig::getJdbcUrl);
        registry.add("spring.datasource.username", DatabaseTestConfig::getUsername);
        registry.add("spring.datasource.password", DatabaseTestConfig::getPassword);
        registry.add("crm.host", () -> crmMockServer.url("/").toString());
    }

    @BeforeEach
    void drainMockServerRequests() throws InterruptedException {
        while (crmMockServer.takeRequest(10, TimeUnit.MILLISECONDS) != null) {
        }
    }
}
