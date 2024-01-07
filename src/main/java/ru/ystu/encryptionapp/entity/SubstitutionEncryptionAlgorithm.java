package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "substitution_encryption_algorithm")
@DiscriminatorValue("substitutionEncryption")
public class SubstitutionEncryptionAlgorithm extends BuiltinEncryptionAlgorithm {
    @Transient
    private Map<Character, Character> substitutionTable;

    public SubstitutionEncryptionAlgorithm() {
        fillSubstitutionTable();
    }

    public SubstitutionEncryptionAlgorithm(Map<Character, Character> substitutionTable) {
        this.substitutionTable = substitutionTable;
    }

    @Override
    public Object encode(String valueToEncode) {
        // Преобразование строки valueToEncode с использованием таблицы замены
        StringBuilder encodedValue = new StringBuilder();
        for (char c : valueToEncode.toCharArray()) {
            // Если символ находится в таблице замены, заменяем его, иначе оставляем без изменений
            if (substitutionTable.containsKey(c)) {
                encodedValue.append(substitutionTable.get(c));
            } else {
                encodedValue.append(c);
            }
        }

        return encodedValue.toString();
    }

    @Override
    public Object decode(String valueToDecode) {
        // Дешифрование строки valueToDecode с использованием обратной таблицы замены
        StringBuilder decodedValue = new StringBuilder();
        Map<Character, Character> reverseTable = new HashMap<>();
        for (Map.Entry<Character, Character> entry : substitutionTable.entrySet()) {
            reverseTable.put(entry.getValue(), entry.getKey());
        }
        for (char c : valueToDecode.toCharArray()) {
            // Если символ находится в обратной таблице замены, заменяем его, иначе оставляем без изменений
            if (reverseTable.containsKey(c)) {
                decodedValue.append(reverseTable.get(c));
            } else {
                decodedValue.append(c);
            }
        }

        return decodedValue.toString();
    }

    private void fillSubstitutionTable() {
        substitutionTable = new HashMap<>();

        // Заполняем таблицу замены для английского алфавита
        for (char c = 'A'; c <= 'Z'; c++) {
            substitutionTable.put(c, c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            substitutionTable.put(c, c);
        }

        // Заполняем таблицу замены для русского алфавита
        for (char c = 'А'; c <= 'Я'; c++) {
            substitutionTable.put(c, c);
        }
        for (char c = 'а'; c <= 'я'; c++) {
            substitutionTable.put(c, c);
        }
    }
}
