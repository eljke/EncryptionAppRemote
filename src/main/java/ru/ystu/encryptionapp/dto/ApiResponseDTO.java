package ru.ystu.encryptionapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public final class ApiResponseDTO extends ApiBaseDTO {
    public static class Builder {
        private final ApiResponseDTO apiResponse;

        public Builder() {
            apiResponse = new ApiResponseDTO();
            apiResponse.response = "No response";
            apiResponse.timestamp = LocalDateTime.now();
        }

        public Builder withStatus(HttpStatus status) {
            apiResponse.status = status;
            return this;
        }

        public Builder withResponse(Object response) {
            apiResponse.response = response;
            return this;
        }

        public ApiResponseDTO build() {
            return apiResponse;
        }
    }
}
