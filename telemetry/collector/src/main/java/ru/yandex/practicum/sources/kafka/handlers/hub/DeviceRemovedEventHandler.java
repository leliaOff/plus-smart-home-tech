package ru.yandex.practicum.sources.kafka.handlers.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.models.hub.DeviceRemovedEvent;
import ru.yandex.practicum.models.hub.HubEvent;
import ru.yandex.practicum.models.hub.enums.HubEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class DeviceRemovedEventHandler extends HubEventHandler<DeviceRemovedEventAvro> {
    public DeviceRemovedEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEvent event) {
        var deviceRemovedEvent = (DeviceRemovedEvent) event;
        return new DeviceRemovedEventAvro(
                deviceRemovedEvent.getId()
        );
    }
}
