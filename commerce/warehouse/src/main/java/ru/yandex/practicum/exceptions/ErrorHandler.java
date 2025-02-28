package ru.yandex.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpecifiedProductAlreadyInWarehouseException(SpecifiedProductAlreadyInWarehouseException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, "Товар уже на складе", ex);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, "Товар не найден", ex);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductInShoppingCartLowQuantityInWarehouse(ProductInShoppingCartLowQuantityInWarehouse ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, "Не достаточное количество товара", ex);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(Throwable ex) {
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", ex);
    }

    private ErrorResponse errorResponse(HttpStatus status, String userMessage, Throwable ex) {

        return new ErrorResponse(ex.getCause(), List.of(ex.getStackTrace()), status.name(), userMessage, ex.getMessage(), List.of(ex.getSuppressed()), ex.getLocalizedMessage());
    }
}
