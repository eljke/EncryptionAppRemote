package ru.ystu.encryptionapp.exception;

import java.util.UUID;

public class EncryptionAlgorithmNotFoundException extends RuntimeException {
    @SuppressWarnings("unused")
    public EncryptionAlgorithmNotFoundException(UUID id) {
        super(String.format("Алгоритм с id = %s не найден", id));
    }

    public EncryptionAlgorithmNotFoundException(String name) {
        super(String.format("Алгоритм '%s' не найден", name));
    }
}
