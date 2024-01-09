package ru.ystu.encryptionapp.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super(String.format("Пользователь с id = %s не найден", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("Пользователь '%s' не найден", username));
    }
}
