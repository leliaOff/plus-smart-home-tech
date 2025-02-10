package ru.yandex.practicum.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.enums.QuantityState;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantityStateRequest {
    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantityState;
}
