package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto {
    @DecimalMin(value = "1.0", message = "must be greater 0.")
    private double width;

    @DecimalMin(value = "1.0", message = "must be greater 0.")
    private double height;

    @DecimalMin(value = "1.0", message = "must be greater 0.")
    private double depth;
}
