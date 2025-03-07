package com.example.advantumconverter;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = {ReactiveUserDetailsServiceAutoConfiguration.class})
public class AdvantumConverterApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AdvantumConverterApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
