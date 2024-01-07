package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "caesar_encryption_algorithm")
@DiscriminatorValue("caesarEncryption")
public class CaesarEncryptionAlgorithm extends BuiltinEncryptionAlgorithm {
    private Integer horizontalOffset;

    public CaesarEncryptionAlgorithm() {
    }

    public CaesarEncryptionAlgorithm(Integer horizontalOffset) {
        this.horizontalOffset = horizontalOffset;
    }

    @Override
    public Object encode(String valueToEncode) {
        StringBuilder encodedValue = new StringBuilder();

        for (int i = 0; i < valueToEncode.length(); i++) {
            char originalChar = valueToEncode.charAt(i);

            // Проверяем, является ли символ буквой русского алфавита
            if (Character.isLetter(originalChar) && originalChar >= 'А' && originalChar <= 'я') {
                char baseChar = Character.isUpperCase(originalChar) ? 'А' : 'а';
                int alphabetSize = 32; // Размер русского алфавита

                char encodedChar = (char) (baseChar + (originalChar - baseChar + horizontalOffset) % alphabetSize);
                encodedValue.append(encodedChar);
            }
            // Проверяем, является ли символ буквой английского алфавита
            else if (Character.isLetter(originalChar) && (originalChar >= 'A' && originalChar <= 'Z' || originalChar >= 'a' && originalChar <= 'z')) {
                char baseChar = Character.isUpperCase(originalChar) ? 'A' : 'a';
                int alphabetSize = 26; // Размер английского алфавита

                char encodedChar = (char) (baseChar + (originalChar - baseChar + horizontalOffset) % alphabetSize);
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

        for (int i = 0; i < valueToDecode.length(); i++) {
            char encodedChar = valueToDecode.charAt(i);

            // Проверяем, является ли символ буквой русского алфавита
            if (Character.isLetter(encodedChar) && encodedChar >= 'А' && encodedChar <= 'я') {
                char baseChar = Character.isUpperCase(encodedChar) ? 'А' : 'а';
                int alphabetSize = 32; // Размер русского алфавита

                char originalChar = (char) (baseChar + (encodedChar - baseChar - horizontalOffset + alphabetSize) % alphabetSize);
                decodedValue.append(originalChar);
            }
            // Проверяем, является ли символ буквой английского алфавита
            else if (Character.isLetter(encodedChar) && (encodedChar >= 'A' && encodedChar <= 'Z' || encodedChar >= 'a' && encodedChar <= 'z')) {
                char baseChar = Character.isUpperCase(encodedChar) ? 'A' : 'a';
                int alphabetSize = 26; // Размер английского алфавита

                char originalChar = (char) (baseChar + (encodedChar - baseChar - horizontalOffset + alphabetSize) % alphabetSize);
                decodedValue.append(originalChar);
            }
            // Если символ не является буквой, оставляем его без изменений
            else {
                decodedValue.append(encodedChar);
            }
        }

        return decodedValue.toString();
    }
}
