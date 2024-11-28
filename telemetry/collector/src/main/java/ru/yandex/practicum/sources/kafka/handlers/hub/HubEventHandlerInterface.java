package ru.yandex.practicum.sources.kafka.handlers.hub;

import ru.yandex.practicum.models.hub.HubEvent;
import ru.yandex.practicum.models.hub.enums.HubEventType;

public interface HubEventHandlerInterface {
    HubEventType getMessageType();

    void handle(HubEvent event);
}
