package ru.pssbd.fonds.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


public enum Role {
    FOUNDER(1, "Основатель"),
    ADMIN(2, "Администратор"),
    STAFF(3, "Сотрудник"),
    USER(4, "Пользователь"),
    DONOR(5, "Донор");

    // Геттеры
    private final int id;
    private final String displayName;

    // Конструктор с ID и названием
    Role(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    // Поиск роли по ID (опционально)
    public static Role getById(int id) {
        for (Role role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("Нет роли с ID: " + id);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
