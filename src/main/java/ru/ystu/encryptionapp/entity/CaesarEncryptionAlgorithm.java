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
@DiscriminatorValue("twoWayDecryptable")
public class CaesarEncryptionAlgorithm extends BuiltinEncryptionAlgorithm {
    @Override
    public Object encode(String valueToEncode) {
        // TODO: Реализовать алгоритм шифрования Цезаря
        return null;
    }
}
