package ru.ystu.encryptionapp.exception;

public class NonDecryptableException extends RuntimeException {
    public NonDecryptableException() {
        super("Message cannot be decoded");
    }

    public NonDecryptableException(String message) {
        super(String.format("Message '%s' cannot be decoded", message));
    }
}
