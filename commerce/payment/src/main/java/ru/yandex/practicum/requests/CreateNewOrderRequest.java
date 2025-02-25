package ru.yandex.practicum.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.ShoppingCartDto;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewOrderRequest {
    @NotNull(message = "необходимо указать идентификатор корзины")
    private ShoppingCartDto shoppingCart;

    @NotNull(message = "необходимо указать адрес доставки")
    private AddressDto deliveryAddress;

    @NotNull(message = "необходимо указать имя пользователя")
    private String username;
}
