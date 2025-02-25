package ru.yandex.practicum.services;

import java.math.BigDecimal;

public class DeliveryCostService {
    public static BigDecimal getCost(String address, BigDecimal BASE_RATE) {
        final String ADDRESS_1 = "ADDRESS_1";
        final String ADDRESS_2 = "ADDRESS_2";
        BigDecimal warehouseMultiplier = BigDecimal.ZERO;
        if (address.contains(ADDRESS_1)) {
            warehouseMultiplier = warehouseMultiplier.add(BigDecimal.ONE);
        }
        if (address.contains(ADDRESS_2)) {
            warehouseMultiplier = warehouseMultiplier.add(BigDecimal.valueOf(2));
        }
        return BASE_RATE.multiply(warehouseMultiplier).add(BASE_RATE);
    }
}
