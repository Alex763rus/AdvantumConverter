package com.example.advantumconverter.enums;

public enum UserRole {

    NEED_SETTING("Требуется настройка"),

    BLOCKED("Заблокирован"),

    EMPLOYEE("Сотрудник"),
    MAIN_EMPLOYEE("Главный сотрудник"),
    SUPPORT("Поддержка"),
    ADMIN("Администратор");

    private String title;

    UserRole(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
