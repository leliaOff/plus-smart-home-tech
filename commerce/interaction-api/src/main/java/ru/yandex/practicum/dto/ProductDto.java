package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID productId;

    @NotBlank
    private String productName;
    @NotBlank
    private String description;

    private String imageSrc;

    @NotNull
    private QuantityState quantityState;

    @NotNull
    private ProductState productState;

    @DecimalMin(value = "1.0", message = "минимальное значение - 1")
    @DecimalMax(value = "5.0", message = "максимальное значение - 5")
    private Double rating;

    @NotNull(message = "необходимо указать категорию")
    private ProductCategory productCategory;

    @DecimalMin(value = "1.0", message = "минимальная цена товара - 1")
    private BigDecimal price;
}
