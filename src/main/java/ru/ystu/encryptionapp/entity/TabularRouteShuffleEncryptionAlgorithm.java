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
@Entity(name = "tabular_route_shuffle_encryption_algorithm")
@DiscriminatorValue("tabularRouteShuffleEncryption")
public class TabularRouteShuffleEncryptionAlgorithm extends BuiltinEncryptionAlgorithm {
    private Integer rowsNumber;
    private Integer columnsNumber;
    private String exitRoute;

    public TabularRouteShuffleEncryptionAlgorithm() {
    }

    public TabularRouteShuffleEncryptionAlgorithm(Integer rowsNumber, Integer columnsNumber, String exitRoute) {
        this.rowsNumber = rowsNumber;
        this.columnsNumber = columnsNumber;
        this.exitRoute = exitRoute;
    }

    @Override
    public Object encode(String valueToEncode) {
        if (valueToEncode.length() > rowsNumber * columnsNumber) {
            throw new IllegalArgumentException("Размер таблицы недостаточен для входных данных.");
        }

        char[][] table = new char[rowsNumber][columnsNumber];

        int index = 0;
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                if (index < valueToEncode.length()) {
                    table[i][j] = valueToEncode.charAt(index);
                    index++;
                } else {
                    table[i][j] = '%';
                }
            }
        }

        StringBuilder encodedValue = createRoute(table, exitRoute);

        return encodedValue.toString();
    }

    @Override
    public Object decode(String valueToDecode) {
        if (valueToDecode.length() > rowsNumber * columnsNumber) {
            throw new IllegalArgumentException("Размер таблицы недостаточен для входных данных.");
        }

        char[][] table = new char[rowsNumber][columnsNumber];

        int index = 0;
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                table[i][j] = valueToDecode.charAt(index);
                index++;
            }
        }

        StringBuilder decodedValue = createRoute(table, exitRoute);

        return decodedValue.toString();
    }

    private StringBuilder createRoute(char[][] table, String route) {
        StringBuilder value = new StringBuilder();

        switch (route) {
            case "UL":
                for (int j = 0; j < columnsNumber; j++) {
                    for (int i = 0; i < rowsNumber; i++) {
                        value.append(table[i][j]);
                    }
                }
                break;
            case "DR":
                for (int j = columnsNumber - 1; j >= 0; j--) {
                    for (int i = rowsNumber - 1; i >= 0; i--) {
                        value.append(table[i][j]);
                    }
                }
                break;
            case "RD":
                for (int i = rowsNumber - 1; i >= 0; i--) {
                    for (int j = columnsNumber - 1; j >= 0; j--) {
                        value.append(table[i][j]);
                    }
                }
                break;
        }

        return value;
    }
}