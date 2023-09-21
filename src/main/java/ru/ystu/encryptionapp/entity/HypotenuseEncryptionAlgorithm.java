package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "hypotenuse_encryption_algorithm")
@DiscriminatorValue("oneWayDecryptable")
public class HypotenuseEncryptionAlgorithm extends CustomEncryptionAlgorithm {
    private Integer horizontalOffset;

    private Integer verticalOffset;

    private Integer rounds;

    @Override
    public Object encode(String valueToEncode) {
        String decodedValue = "";

        for (int i = 0; i < rounds; i++) {
            byte[] bytesEncoded = getBytesEncoded(valueToEncode);

            StringBuilder hexString = new StringBuilder();
            // Переводим байты в шестнадцатеричное представление
            for (byte b : bytesEncoded) {
                hexString.append(String.format("%02X", b));
            }

            int len = hexString.toString().length();

            // Обратно из шестнадцатеричного представления в байты
            byte[] data = new byte[len / 2];
            for (int j = 0; j < len; j += 2) {
                data[j / 2] = (byte) ((Character.digit(hexString.charAt(j), 16) << 4)
                        + Character.digit(hexString.charAt(j + 1), 16));
            }

            decodedValue = new String(data, StandardCharsets.UTF_8);
        }

        return decodedValue;
    }

    private byte[] getBytesEncoded(String valueToEncode) {
        StringBuilder encodedValue = new StringBuilder();
        for (char c : valueToEncode.toCharArray()) {
            // Вычисляем гипотенузу и округляем до ближайшего целого
            double hypotenuse = Math.sqrt(Math.pow(horizontalOffset, 2) + Math.pow(verticalOffset, 2));
            int roundedHypotenuse = (int) Math.round(hypotenuse);

            // Смещаем букву в алфавите
            char encodedChar = (char) (c + roundedHypotenuse);

            encodedValue.append(encodedChar);
        }

        return encodedValue.toString().getBytes();
    }
}
