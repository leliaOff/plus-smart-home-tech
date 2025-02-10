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
public class AddProductToWarehouseRequest {
    @NotNull(message = "необходимо указать ID")
    private UUID productId;

    @Min(value = 1, message = "минимальное значение количества - 1")
    private int quantity;
}
