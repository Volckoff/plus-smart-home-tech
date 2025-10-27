package ru.practicum.exceptions;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    Throwable cause;

    StackTraceElement[] stackTrace;

    HttpStatus httpStatus;

    String userMessage;

    String message;

    Throwable[] suppressed;

    String localizedMessage;
}