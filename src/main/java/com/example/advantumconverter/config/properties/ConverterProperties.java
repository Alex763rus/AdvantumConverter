package com.example.advantumconverter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "converter")
@Data
public class ConverterProperties {

    private ConverterSettings bogorodsk;
    private ConverterSettings dominos;
    private ConverterSettings cofix;
    private ConverterSettings samokat;
    private ConverterSettings agroprom;
    private ConverterSettings agropromDetail;

    @Data
    public static class ConverterSettings {
        private Boolean enabled;
    }
}
