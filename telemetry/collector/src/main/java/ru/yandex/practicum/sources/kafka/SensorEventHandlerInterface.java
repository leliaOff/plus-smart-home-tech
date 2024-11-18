package ru.yandex.practicum.sources.kafka;

import ru.yandex.practicum.models.sensor.SensorEvent;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;

public interface SensorEventHandlerInterface {
    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
