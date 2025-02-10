package ru.yandex.practicum.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProductQuantityRequest {

    @NotNull(message = "необходимо указать идентификатор товара")
    private UUID productId;

    @Min(value = 0, message = "минимальное значение количества - 0")
    private int newQuantity;
}
