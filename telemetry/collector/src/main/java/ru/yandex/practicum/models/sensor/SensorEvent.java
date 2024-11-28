package ru.yandex.practicum.models.sensor;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;

import java.time.Instant;

@Getter
@Setter
@ToString
public abstract class SensorEvent {
    @NotBlank
    private String id;
    @NotBlank
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract SensorEventType getType();
}
