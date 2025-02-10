package com.example.advantumconverter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "crm")
@Data
public class CrmConfigProperties {

    private CrmCreds sber;
    private CrmCreds ozon;

    @Data
    public static class CrmCreds {
        private String login;
        private String password;
    }
}
