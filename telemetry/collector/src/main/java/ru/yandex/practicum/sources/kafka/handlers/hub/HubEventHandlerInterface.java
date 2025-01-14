package ru.yandex.practicum.sources.kafka.handlers.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventHandlerInterface {
    HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto event);
}
