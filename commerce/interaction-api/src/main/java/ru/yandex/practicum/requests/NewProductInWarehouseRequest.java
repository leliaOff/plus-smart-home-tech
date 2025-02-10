package ru.yandex.practicum.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.dto.DimensionDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseRequest {
    @NotNull(message = "необходимо указать ID")
    private UUID productId;

    @NotNull(message = "необходимо указать признак хрупкого товара")
    private boolean fragile;

    @NotNull(message = "необходимо указать габариты товара")
    private DimensionDto dimension;

    @DecimalMin(value = "1.0", message = "минимальное значение веса - 1")
    private double weight;
}
