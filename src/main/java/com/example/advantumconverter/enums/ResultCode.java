package com.example.advantumconverter.enums;

public enum ResultCode {

    OK("Ок"),
    WARNING("Предпреждение"),
    ERROR("Ошибка");

    private String title;

    ResultCode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
