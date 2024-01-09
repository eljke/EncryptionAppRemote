package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "vigenere_encryption_algorithm")
@DiscriminatorValue("vigenereEncryption")
public class VigenereEncryptionAlgorithm extends BuiltinEncryptionAlgorithm {
    @Column(name = "'key")
    private String key;

    public VigenereEncryptionAlgorithm() {
    }

    public VigenereEncryptionAlgorithm(String key) {
        this.key = key;
    }

    @Override
    public Object encode(String valueToEncode) {
        StringBuilder encodedValue = new StringBuilder();
        for (int i = 0, len = valueToEncode.length(); i < len; i++) {
            char originalChar = valueToEncode.charAt(i);

            // Проверяем, является ли символ буквой русского алфавита
            char keyChar = key.toLowerCase().charAt(i % key.length());
            if (Character.isLetter(originalChar) && originalChar >= 'А' && originalChar <= 'я') {
                char baseChar = Character.isUpperCase(originalChar) ? 'А' : 'а';
                int alphabetSize = 32; // Размер русского алфавита

                int shift = keyChar - baseChar;
                char encodedChar = (char) (baseChar + (originalChar - baseChar + shift) % alphabetSize);

                encodedValue.append(encodedChar);
            }
            // Проверяем, является ли символ буквой английского алфавита
            else if (Character.isLetter(originalChar) && (originalChar >= 'A' && originalChar <= 'Z' || originalChar >= 'a' && originalChar <= 'z')) {
                char baseChar = Character.isUpperCase(originalChar) ? 'A' : 'a';
                int alphabetSize = 26; // Размер английского алфавита
                int shift = keyChar - baseChar;
                char encodedChar = (char) (baseChar + (originalChar - baseChar + shift) % alphabetSize);

                encodedValue.append(encodedChar);
            }
            // Если символ не является буквой, оставляем его без изменений
            else {
                encodedValue.append(originalChar);
            }
        }

        return encodedValue.toString();
    }

    @Override
    public Object decode(String valueToDecode) {
        StringBuilder decodedValue = new StringBuilder();
        for (int i = 0, len = valueToDecode.length(); i < len; i++) {
            char originalChar = valueToDecode.charAt(i);

            // Проверяем, является ли символ буквой русского алфавита
            char keyChar = key.toLowerCase().charAt(i % key.length());
            if (Character.isLetter(originalChar) && originalChar >= 'А' && originalChar <= 'я') {
                char baseChar = Character.isUpperCase(originalChar) ? 'А' : 'а';
                int alphabetSize = 32; // Размер русского алфавита
                int shift = keyChar - baseChar;
                char decodedChar = (char) (baseChar + (originalChar - baseChar - shift + alphabetSize) % alphabetSize);

                decodedValue.append(decodedChar);
            }
            // Проверяем, является ли символ буквой английского алфавита
            else if (Character.isLetter(originalChar) && (originalChar >= 'A' && originalChar <= 'Z' || originalChar >= 'a' && originalChar <= 'z')) {
                char baseChar = Character.isUpperCase(originalChar) ? 'A' : 'a';
                int alphabetSize = 26; // Размер английского алфавита
                int shift = keyChar - baseChar;
                char decodedChar = (char) (baseChar + (originalChar - baseChar - shift + alphabetSize) % alphabetSize);

                decodedValue.append(decodedChar);
            }
            // Если символ не является буквой, оставляем его без изменений
            else {
                decodedValue.append(originalChar);
            }
        }

        return decodedValue.toString();
    }
}
