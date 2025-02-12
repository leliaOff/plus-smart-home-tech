package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableDto {
    @NotNull(message = "необходимо указать страницу")
    @Min(value = 0, message = "минимальное значение страницы - 0")
    private Integer page;

    @NotNull(message = "необходимо указать указать размер списка")
    @Min(value = 1, message = "минимальное значение размера списка - 1")
    private Integer size;

    private List<String> sort;
}
