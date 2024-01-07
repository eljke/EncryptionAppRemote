package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ystu.encryptionapp.enumeration.DecryptionDirection;

import java.util.UUID;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "encryption_algorithm")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "algorithm_type")
public abstract class EncryptionAlgorithm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "decryption_direction")
    private DecryptionDirection decryptionDirection;

    public EncryptionAlgorithm() {
    }

    public EncryptionAlgorithm(String name) {
        this.name = name;
    }

    public EncryptionAlgorithm(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract Object encode(String valueToEncode);

    public abstract Object decode(String valueToDecode);
}
