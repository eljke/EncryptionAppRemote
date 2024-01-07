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
@Entity(name = "hypotenuse_encryption_algorithm")
@DiscriminatorValue("hypotenuseEncryption")
public class HypotenuseEncryptionAlgorithm extends CustomEncryptionAlgorithm {
    private Integer horizontalOffset;

    private Integer verticalOffset;

    private Integer rounds;

    private String alphabet;

    @Transient
    private final char[][] russianPolybiusSquare = Alphabet.RUSSIAN.getCharacters();

    @Transient
    private final char[][] englishPolybiusSquare = Alphabet.ENGLISH.getCharacters();

    public HypotenuseEncryptionAlgorithm() {
    }

    @SuppressWarnings("unused")
    public HypotenuseEncryptionAlgorithm(Integer bothSideOffset) {
        this.horizontalOffset = bothSideOffset;
        this.verticalOffset = bothSideOffset;
        this.rounds = 1;
        this.alphabet = "RU";
    }

    @SuppressWarnings("unused")
    public HypotenuseEncryptionAlgorithm(Integer horizontalOffset, Integer verticalOffset) {
        this.horizontalOffset = horizontalOffset;
        this.verticalOffset = verticalOffset;
        this.rounds = 1;
        this.alphabet = "RU";
    }

    @SuppressWarnings("unused")
    public HypotenuseEncryptionAlgorithm(Integer horizontalOffset, Integer verticalOffset, Integer rounds) {
        this.horizontalOffset = horizontalOffset;
        this.verticalOffset = verticalOffset;
        this.rounds = rounds;
        this.alphabet = "RU";
    }

    public HypotenuseEncryptionAlgorithm(Integer horizontalOffset, Integer verticalOffset, Integer rounds, String alphabet) {
        this.horizontalOffset = horizontalOffset;
        this.verticalOffset = verticalOffset;
        this.rounds = rounds;
        this.alphabet = alphabet;
    }

    @Override
    public Object encode(String valueToEncode) {
        for (int i = 0; i < rounds; i++) {
            valueToEncode = encodeRound(valueToEncode);
        }

        return valueToEncode;
    }

    private String encodeRound(String valueToEncode) {
        valueToEncode = valueToEncode.toUpperCase();
        StringBuilder encodedValue = new StringBuilder();

        if (alphabet.equals("RU")) {
            for (char c : valueToEncode.toCharArray()) {
                for (int i = 0; i < Alphabet.RUSSIAN.getRowsNumber(); i++) {
                    for (int j = 0; j < Alphabet.RUSSIAN.getColumnsNumber(); j++) {
                        if (c == russianPolybiusSquare[j][i]) {
                            char encodedChar;
                            encodedChar =
                                    russianPolybiusSquare
                                            [(j + verticalOffset) % Alphabet.RUSSIAN.getColumnsNumber()]
                                            [(i + horizontalOffset) % Alphabet.RUSSIAN.getRowsNumber()];
                            encodedValue.append(encodedChar);
                        }
                    }
                }
            }
        } else if (alphabet.equals("EN")) {
            for (char c : valueToEncode.toCharArray()) {
                for (int i = 0; i < Alphabet.ENGLISH.getRowsNumber(); i++) {
                    for (int j = 0; j < Alphabet.ENGLISH.getColumnsNumber(); j++) {
                        if (c == englishPolybiusSquare[j][i]) {
                            char encodedChar;
                            encodedChar =
                                    englishPolybiusSquare
                                            [(j + verticalOffset) % Alphabet.ENGLISH.getColumnsNumber()]
                                            [(i + horizontalOffset) % Alphabet.ENGLISH.getRowsNumber()];
                            encodedValue.append(encodedChar);
                        }
                    }
                }
            }
        }

        return encodedValue.toString();
    }

    @Override
    public Object decode(String valueToDecode) {
        for (int i = 0; i < rounds; i++) {
            valueToDecode = decodeRound(valueToDecode);
        }

        return valueToDecode;
    }

    private String decodeRound(String valueToDecode) {
        valueToDecode = valueToDecode.toUpperCase();
        StringBuilder decodedValue = new StringBuilder();

        if (alphabet.equals("RU")) {
            for (char c : valueToDecode.toCharArray()) {
                for (int i = 0; i < Alphabet.RUSSIAN.getRowsNumber(); i++) {
                    for (int j = 0; j < Alphabet.RUSSIAN.getColumnsNumber(); j++) {
                        if (c == russianPolybiusSquare[j][i]) {
                            char decodedChar;
                            decodedChar =
                                    russianPolybiusSquare
                                            [Math.floorMod(j - verticalOffset, Alphabet.RUSSIAN.getColumnsNumber())]
                                            [Math.floorMod(i - horizontalOffset, Alphabet.RUSSIAN.getRowsNumber())];
                            decodedValue.append(decodedChar);
                        }
                    }
                }
            }
        } else if (alphabet.equals("EN")) {
            for (char c : valueToDecode.toCharArray()) {
                for (int i = 0; i < Alphabet.ENGLISH.getRowsNumber(); i++) {
                    for (int j = 0; j < Alphabet.ENGLISH.getColumnsNumber(); j++) {
                        if (c == englishPolybiusSquare[j][i]) {
                            char decodedChar;
                            decodedChar =
                                    englishPolybiusSquare
                                            [Math.floorMod(j - verticalOffset, Alphabet.ENGLISH.getColumnsNumber())]
                                            [Math.floorMod(i - horizontalOffset, Alphabet.ENGLISH.getRowsNumber())];
                            decodedValue.append(decodedChar);
                        }
                    }
                }
            }
        }

        return decodedValue.toString();
    }
}