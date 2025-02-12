package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto {
    @DecimalMin(value = "1.0", message = "минимально допустимое значение - 1")
    private Double width;

    @DecimalMin(value = "1.0", message = "минимально допустимое значение - 1")
    private Double height;

    @DecimalMin(value = "1.0", message = "минимально допустимое значение - 1")
    private Double depth;
}
