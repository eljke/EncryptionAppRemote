package ru.ystu.encryptionapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.ystu.encryptionapp.enumeration.AlgorithmType;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class EncodeRequestDTO {
    @JsonProperty("algorithm")
    private AlgorithmType algorithmType;
    @JsonProperty("params")
    private List<Object> params;
    @JsonProperty("value")
    private String valueToEncode;
}
