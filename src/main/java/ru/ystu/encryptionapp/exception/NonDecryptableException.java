package ru.ystu.encryptionapp.exception;

public class NonDecryptableException extends RuntimeException {
    public NonDecryptableException() {
        super("Значение, зашифрованное таким способом, нельзя расшифровать");
    }

    @SuppressWarnings("unused")
    public NonDecryptableException(String message) {
        super(String.format("Значение '%s', зашифрованное таким способом, нельзя расшифровать", message));
    }
}
