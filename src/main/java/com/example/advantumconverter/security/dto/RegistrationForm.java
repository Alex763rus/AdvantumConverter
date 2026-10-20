package com.example.advantumconverter.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {
    private String username;
    private String password;
    private Long telegramChatId;
}
