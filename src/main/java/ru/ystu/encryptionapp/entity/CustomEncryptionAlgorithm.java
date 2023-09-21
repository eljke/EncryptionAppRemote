package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ystu.encryptionapp.enumeration.AlgorithmPrinciple;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "custom_encryption_algorithm")
@DiscriminatorValue("custom")
public abstract class CustomEncryptionAlgorithm extends EncryptionAlgorithm {
    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm_principle")
    private AlgorithmPrinciple algorithmPrinciple;
}
