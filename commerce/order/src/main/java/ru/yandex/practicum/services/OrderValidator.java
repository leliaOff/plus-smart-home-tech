package ru.yandex.practicum.services;

import ru.yandex.practicum.exceptions.NotAuthorizedUserException;

public class OrderValidator {
    public void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Не указано имя пользователя");
        }
    }
}
