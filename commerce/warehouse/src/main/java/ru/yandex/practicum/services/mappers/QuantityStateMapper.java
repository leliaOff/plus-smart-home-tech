package ru.yandex.practicum.services.mappers;

import ru.yandex.practicum.enums.QuantityState;

public class QuantityStateMapper {
    private static final int LIMIT_COUNT = 5;
    private static final int ENOUGH_COUNT = 20;

    public static QuantityState toQuantityState(int quantity) {
        if (quantity == 0) {
            return QuantityState.ENDED;
        }
        if (quantity > 0 && quantity < LIMIT_COUNT) {
            return QuantityState.FEW;
        }
        if (quantity >= LIMIT_COUNT && quantity <= ENOUGH_COUNT) {
            return QuantityState.ENOUGH;
        }
        return QuantityState.MANY;
    }
}
