package ru.ystu.encryptionapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ApiErrorDTO.class, name = "error"),
        @JsonSubTypes.Type(value = ApiResponseDTO.class, name = "response")
})
@ToString
public abstract sealed class ApiBaseDTO permits ApiErrorDTO, ApiResponseDTO {
    protected HttpStatus status;
    protected Object response;
    protected LocalDateTime timestamp;

    public ApiBaseDTO() {
        this.status = HttpStatus.OK;
        this.response = "No response";
        this.timestamp = LocalDateTime.now();
    }
}
