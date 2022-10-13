package ru.practicum.explore_with_me.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private final HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public ApiError(HttpStatus status) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public ApiError(HttpStatus status, String message, String reason) {
        this(status, reason);
        this.message = message;
    }

    public ApiError(HttpStatus status, String reason) {
        this(status);
        this.reason = reason;
    }
}
