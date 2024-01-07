package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ystu.encryptionapp.enumeration.Alphabet;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "polybius_square_encryption_algorithm")
@DiscriminatorValue("polybiusSquareEncryption")
public class PolybiusSquareEncryptionAlgorithm extends BuiltinEncryptionAlgorithm {
    private String alphabet;

    // Version 1
    @Transient
    private final char[][] russianPolybiusSquare = Alphabet.RUSSIAN.getCharacters();

    // Version 2
//    @Transient
//    private final char[][] russianPolybiusSquare = {
//            {'А', 'Б', 'В', 'Г', 'Д', 'Е'},
//            {'Ж', 'З', 'И', 'К', 'Л', 'М'},
//            {'Н', 'О', 'П', 'Р', 'С', 'Т'},
//            {'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш'},
//            {'Щ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'}
//    };

    @Transient
    private final char[][] englishPolybiusSquare = Alphabet.ENGLISH.getCharacters();

    public PolybiusSquareEncryptionAlgorithm() {
        this.alphabet = "EN";
    }

    public PolybiusSquareEncryptionAlgorithm(String alphabet) {
        this.alphabet = alphabet;
    }

    @Override
    public Object encode(String valueToEncode) {
        valueToEncode = valueToEncode.toUpperCase();
        StringBuilder encodedValue = new StringBuilder();

        for (int i = 0; i < valueToEncode.length(); i++) {
            char originalChar = valueToEncode.charAt(i);

            if (alphabet.equals("RU")) {
                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < 6; col++) {
                        if (russianPolybiusSquare[row][col] == originalChar) {
                            encodedValue.append(row + 1).append(col + 1);
                        }
                    }
                }
            }
            else if (alphabet.equals("EN")) {
                if (originalChar == 'J') {
                    originalChar = 'I';
                }

                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < 5; col++) {
                        if (englishPolybiusSquare[row][col] == originalChar) {
                            encodedValue.append(row + 1).append(col + 1);
                        }
                    }
                }
            }
        }

        StringBuilder reorderedText = new StringBuilder();
        int length = encodedValue.length();
        for (int i = 1; i < length; i += 2) {
            reorderedText.append(encodedValue.charAt(i));
        }
        for (int i = 0; i < length; i += 2) {
            reorderedText.append(encodedValue.charAt(i));
        }

        StringBuilder word = new StringBuilder();
        if (alphabet.equals("EN")) {
            for (int i = 0; i < reorderedText.length(); i += 2) {
                word.append(englishPolybiusSquare[Character.getNumericValue(reorderedText.charAt(i + 1)) - 1][Character.getNumericValue(reorderedText.charAt(i)) - 1]);
            }
        } else if (alphabet.equals("RU")) {
            for (int i = 0; i < reorderedText.length(); i += 2) {
                word.append(russianPolybiusSquare[Character.getNumericValue(reorderedText.charAt(i + 1)) - 1][Character.getNumericValue(reorderedText.charAt(i)) - 1]);
            }
        }

        return word.toString();
    }

    @Override
    public Object decode(String valueToDecode) {
        valueToDecode = valueToDecode.toUpperCase();
        StringBuilder decodedValue = new StringBuilder();

        for (int i = 0; i < valueToDecode.length(); i++) {
            char originalChar = valueToDecode.charAt(i);
            if (alphabet.equals("RU")) {
                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < 6; col++) {
                        if (russianPolybiusSquare[row][col] == originalChar) {
                            decodedValue.append(col + 1).append(row + 1);
                        }
                    }
                }
            }
            else if (alphabet.equals("EN")) {
                if (originalChar == 'J') {
                    originalChar = 'I';
                }

                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < 5; col++) {
                        if (englishPolybiusSquare[row][col] == originalChar) {
                            decodedValue.append(col + 1).append(row + 1);
                        }
                    }
                }
            }
        }

        StringBuilder reorderedText = new StringBuilder();
        int length = decodedValue.length();
        for (int i = 0; i < length; i++) {
            if (i == length / 2) {
                break;
            }
            reorderedText.append(decodedValue.charAt(i));
            reorderedText.append(decodedValue.charAt(i + length / 2));
        }

        StringBuilder word = new StringBuilder();
        if (alphabet.equals("EN")) {
            for (int i = 0; i < reorderedText.length(); i += 2) {
                word.append(englishPolybiusSquare[Character.getNumericValue(reorderedText.charAt(i + 1)) - 1][Character.getNumericValue(reorderedText.charAt(i)) - 1]);
            }
        } else if (alphabet.equals("RU")) {
            for (int i = 0; i < reorderedText.length(); i += 2) {
                word.append(russianPolybiusSquare[Character.getNumericValue(reorderedText.charAt(i + 1)) - 1][Character.getNumericValue(reorderedText.charAt(i)) - 1]);
            }
        }

        return word.toString();
    }
}