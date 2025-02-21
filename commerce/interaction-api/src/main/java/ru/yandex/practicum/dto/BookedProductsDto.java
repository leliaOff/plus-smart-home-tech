package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {
    @DecimalMin(value = "1.0", message = "минимально допустимое значение - 1")
    private Double deliveryWeight;
    @DecimalMin(value = "1.0", message = "минимально допустимое значение - 1")
    private Double deliveryVolume;
    @NotNull
    private Boolean fragile;
}
