package ru.yandex.practicum.models.sensor;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {
    @NotBlank
    private int temperatureC;
    @NotBlank
    private int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
