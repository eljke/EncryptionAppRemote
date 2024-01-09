package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.ystu.encryptionapp.exception.NonDecryptableException;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "bcrypt_encryption_algorithm")
@DiscriminatorValue("bcryptEncryption")
public class BCryptEncryptionAlgorithm extends BuiltinEncryptionAlgorithm {
    private Integer strength;

    @Transient
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BCryptEncryptionAlgorithm() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public BCryptEncryptionAlgorithm(Integer strength) {
        this.strength = strength;
        bCryptPasswordEncoder = new BCryptPasswordEncoder(strength);
    }

    @Override
    public Object encode(String valueToEncode) {
        return bCryptPasswordEncoder.encode(valueToEncode);
    }

    @Override
    public Object decode(String valueToDecode) {
        throw new NonDecryptableException();
    }

    @SuppressWarnings("unused")
    public boolean matches(String rawValue, String encodedValue) {
        return bCryptPasswordEncoder.matches(rawValue, encodedValue);
    }
}
