package ru.yandex.practicum.sources.kafka.handlers.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.models.hub.DeviceAddedEvent;
import ru.yandex.practicum.models.hub.HubEvent;
import ru.yandex.practicum.models.hub.enums.HubEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class DeviceAddedEventHandler extends HubEventHandler<DeviceAddedEventAvro> {
    public DeviceAddedEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        var deviceAddedEvent = (DeviceAddedEvent) event;
        return new DeviceAddedEventAvro(
                deviceAddedEvent.getId(),
                DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name())
        );
    }
}
