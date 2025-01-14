package ru.yandex.practicum.sources.kafka.handlers.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandlerInterface {
    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto event);
}
