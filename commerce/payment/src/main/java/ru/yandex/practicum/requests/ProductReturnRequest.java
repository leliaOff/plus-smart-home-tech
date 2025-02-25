package ru.yandex.practicum.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReturnRequest {
    @NotNull(message = "необходимо указать идентификатор заказа")
    private UUID orderId;

    private Map<UUID, Integer> products;
}
