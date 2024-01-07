package ru.ystu.encryptionapp.exception;

import java.util.UUID;

public class EncryptionAlgorithmNotFoundException extends RuntimeException {
    // TODO: Поправить
    public EncryptionAlgorithmNotFoundException(UUID id) {
        super(String.format("Algorithm with id = %s not found", id));
    }

    public EncryptionAlgorithmNotFoundException(String name) {
        super(String.format("Algorithm %s not found", name));
    }
}
