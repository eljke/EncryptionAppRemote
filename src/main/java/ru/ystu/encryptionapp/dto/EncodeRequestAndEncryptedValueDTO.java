package ru.ystu.encryptionapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class EncodeRequestAndEncryptedValueDTO {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("encodeRequest")
    private EncodeRequestDTO encodeRequest;
    @JsonProperty("encodedValue")
    private String encodedValue;
    @JsonProperty("user")
    private String user;
    @JsonProperty("date")
    private String date;
}
