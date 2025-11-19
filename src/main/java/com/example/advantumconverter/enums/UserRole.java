package com.example.advantumconverter.enums;

public enum UserRole {

    /* 0 */ NEED_SETTING("Требуется настройка"),
    /* 1 */ BLOCKED("Заблокирован"),
    /* 2 */ EMPLOYEE("Сотрудник"),
    /* 3 */ MAIN_EMPLOYEE("Главный сотрудник"),
    /* 4 */ SUPPORT("Поддержка"),
    /* 5 */ ADMIN("Администратор"),
    /* 6 */ EMPLOYEE_API("Сотрудник апи"),
    /* 7 */ EMPLOYEE_RS("Сотрудник РС");

    private String title;

    UserRole(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
