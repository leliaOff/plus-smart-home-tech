package ru.yandex.practicum.sources.kafka.handlers.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.models.sensor.LightSensorEvent;
import ru.yandex.practicum.models.sensor.SensorEvent;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class LightSensorEventHandler extends SensorEventHandler<LightSensorAvro> {
    public LightSensorEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
        var lightEvent = (LightSensorEvent) event;
        return new LightSensorAvro(
                lightEvent.getLinkQuality(),
                lightEvent.getLuminosity()
        );
    }
}
