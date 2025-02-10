package ru.yandex.practicum.services;

import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.exceptions.NotAuthorizedUserException;
import ru.yandex.practicum.exceptions.ProductNotAvailableException;

public class ShoppingCartValidator {
    private static final int LIMITED_COUNT = 5;
    private static final int ENOUGH_COUNT = 20;
    public void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Не указано имя пользователя");
        }
    }

    public void validateProductQuantity(ProductDto productDto, int quantity) {
        if (productDto.getProductState() != ProductState.ACTIVE) {
            throw new ProductNotAvailableException("Товар не доступен: " + productDto.getProductId());
        }
        QuantityState quantityState = productDto.getQuantityState();
        switch (quantityState) {
            case ENDED -> throw new ProductNotAvailableException("Товар не доступен: " + productDto.getProductId());
            case FEW -> {
                if (quantity > LIMITED_COUNT) {
                    throw new ProductNotAvailableException("Товар не доступен к заказу: " + productDto.getProductId());
                }
            }
            case ENOUGH -> {
                if (quantity >= ENOUGH_COUNT) {
                    throw new ProductNotAvailableException("Товар не доступен к заказу: " + productDto.getProductId());
                }
            }
            default -> throw new IllegalStateException("Не определенное значение перечисления: " + quantityState);
        }
    }
}
