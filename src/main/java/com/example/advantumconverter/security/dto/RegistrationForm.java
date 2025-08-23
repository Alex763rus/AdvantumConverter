package com.example.advantumconverter.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {
    private String username;
    private String fullName;
    private Long telegramChatId;
}