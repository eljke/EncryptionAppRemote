package ru.ystu.encryptionapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ystu.encryptionapp.enumeration.AlgorithmType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "encode_request_and_encrypted_value")
public class EncodeRequestAndEncryptedValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "algorithm")
    private AlgorithmType algorithmType;

    @Column(name = "params")
    private String params;

    @Column(name = "value_to_encode")
    private String valueToEncode;

    @Column(name = "encoded_value")
    private String encodedValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "date")
    private LocalDateTime date = LocalDateTime.now();
}
