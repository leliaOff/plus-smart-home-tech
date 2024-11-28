package ru.yandex.practicum.sources.kafka.handlers.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.models.sensor.SensorEvent;
import ru.yandex.practicum.models.sensor.SwitchSensorEvent;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class SwitchSensorEventHandler extends SensorEventHandler<SwitchSensorAvro> {
    public SwitchSensorEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        var switchEvent = (SwitchSensorEvent) event;
        return new SwitchSensorAvro(
                switchEvent.isState()
        );
    }
}
