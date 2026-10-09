package com.example.advantumconverter.model.jpa;

public interface CommandStatProjection {

    String getMessageText();

    Long getTotalCount();

    Long getBotCount();

    Long getWebCount();

    Long getUserCount();
}
