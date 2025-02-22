package ru.yandex.practicum.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyProductsRequest {
    @NotNull(message = "не указан идентификатор корзины")
    private UUID shoppingCartId;

    @NotNull(message = "не указан идентификатор заказа")
    private UUID orderId;
}
