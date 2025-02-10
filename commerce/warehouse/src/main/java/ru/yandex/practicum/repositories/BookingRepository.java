package ru.yandex.practicum.repositories;

import ru.yandex.practicum.models.Booking;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
    Optional<Booking> findByShoppingCartId(UUID shoppingCartId);
}
