package com.example.advantumconverter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "crm")
@Data
public class BotConfigProperties {

    private String host;
    private Integer connectTimeoutMillis;

    private CrmCreds artfruit;
    private CrmCreds sber;
    private CrmCreds ozon;
    private CrmCreds lenta;

    @Data
    public static class CrmCreds {
        private String login;
        private String password;
    }
}
