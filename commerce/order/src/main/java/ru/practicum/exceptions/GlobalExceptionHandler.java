package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("NotFoundException occurred: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.NOT_FOUND)
                .userMessage(e.getMessage())
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAuthorizedUserException(final NotAuthorizedUserException e) {
        log.error("NotAuthorizedUserException occurred: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .userMessage(e.getMessage())
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoOrderFoundException(final NoOrderFoundException e) {
        log.error("NoOrderFoundException occurred: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .userMessage(e.getMessage())
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSpecifiedProductInWarehouseException(final NoSpecifiedProductInWarehouseException e) {
        log.error("NoSpecifiedProductInWarehouseException occurred: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .userMessage(e.getMessage())
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException occurred: {}", e.getMessage(), e);
        
        if (e.getParameter() != null && e.getParameter().getParameterName() != null) {
            String paramName = e.getParameter().getParameterName();
            if ("username".equals(paramName) || "userName".equals(paramName)) {
                String errorMessage = "Имя пользователя не должно быть пустым";
                return ErrorResponse.builder()
                        .cause(e.getCause())
                        .stackTrace(e.getStackTrace())
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .userMessage(errorMessage)
                        .message(e.getMessage())
                        .suppressed(e.getSuppressed())
                        .localizedMessage(e.getLocalizedMessage())
                        .build();
            }
        }
        
        if (e.getBindingResult().getFieldErrors().stream()
                .anyMatch(error -> "username".equals(error.getField()) || 
                                  "userName".equals(error.getField()))) {
            String errorMessage = "Имя пользователя не должно быть пустым";
            return ErrorResponse.builder()
                    .cause(e.getCause())
                    .stackTrace(e.getStackTrace())
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .userMessage(errorMessage)
                    .message(e.getMessage())
                    .suppressed(e.getSuppressed())
                    .localizedMessage(e.getLocalizedMessage())
                    .build();
        }
        
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .userMessage("Data validation error")
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.error("ConstraintViolationException occurred: {}", e.getMessage(), e);
        
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            if (propertyPath.contains("username") || propertyPath.contains("userName")) {
                String errorMessage = "Имя пользователя не должно быть пустым";
                return ErrorResponse.builder()
                        .cause(e.getCause())
                        .stackTrace(e.getStackTrace())
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .userMessage(errorMessage)
                        .message(e.getMessage())
                        .suppressed(e.getSuppressed())
                        .localizedMessage(e.getLocalizedMessage())
                        .build();
            }
        }
        
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .userMessage("Validation error")
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException occurred: {}", e.getMessage(), e);
        
        if (e.getName() != null && ("username".equals(e.getName()) || "userName".equals(e.getName()))) {
            String errorMessage = "Имя пользователя не должно быть пустым";
            return ErrorResponse.builder()
                    .cause(e.getCause())
                    .stackTrace(e.getStackTrace())
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .userMessage(errorMessage)
                    .message(e.getMessage())
                    .suppressed(e.getSuppressed())
                    .localizedMessage(e.getLocalizedMessage())
                    .build();
        }
        
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .userMessage("Invalid request parameter")
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

}
