package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSpecifiedProductInWarehouseException(final NoSpecifiedProductInWarehouseException e) {
        log.error("NoSpecifiedProductInWarehouseException occurred: {}", e.getMessage(), e);
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductInShoppingCartLowQuantityInWarehouseException(final ProductInShoppingCartLowQuantityInWarehouseException e) {
        log.error("ProductInShoppingCartLowQuantityInWarehouseException occurred: {}", e.getMessage(), e);
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
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleSpecifiedProductAlreadyInWarehouseException(final SpecifiedProductAlreadyInWarehouseException e) {
        log.error("SpecifiedProductAlreadyInWarehouseException occurred: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.CONFLICT)
                .userMessage(e.getMessage())
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException occurred: {}", e.getMessage(), e);
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(final Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .userMessage("Iternal Server Error")
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}

