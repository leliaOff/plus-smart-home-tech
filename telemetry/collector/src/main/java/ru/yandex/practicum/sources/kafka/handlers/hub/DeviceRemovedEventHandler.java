package ru.yandex.practicum.sources.kafka.handlers.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class DeviceRemovedEventHandler extends HubEventHandler<DeviceRemovedEventAvro> {
    public DeviceRemovedEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEventProto event) {
        DeviceRemovedEventProto deviceRemovedEvent = event.getDeviceRemoved();
        return new DeviceRemovedEventAvro(
                deviceRemovedEvent.getId()
        );
    }
}
