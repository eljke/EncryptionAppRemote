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
public final class ApiErrorDTO extends ApiBaseDTO {
    private Throwable cause;

    public static class Builder {
        private final ApiErrorDTO apiError;

        public Builder() {
            apiError = new ApiErrorDTO();
            apiError.response = "No response";
            apiError.cause = null;
            apiError.timestamp = LocalDateTime.now();
        }

        public Builder withStatus(HttpStatus status) {
            apiError.status = status;
            return this;
        }

        public Builder withResponse(Object response) {
            apiError.response = response;
            return this;
        }

        public Builder withCause(Throwable cause) {
            apiError.cause = cause;
            return this;
        }

        public ApiErrorDTO build() {
            return apiError;
        }
    }
}
