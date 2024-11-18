package ru.yandex.practicum.sources.kafka.handlers.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.models.sensor.SensorEvent;
import ru.yandex.practicum.models.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class TemperatureSensorEventHandler extends SensorEventHandler<TemperatureSensorAvro> {
    public TemperatureSensorEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        var temperatureEvent = (TemperatureSensorEvent) event;
        return new TemperatureSensorAvro(
                temperatureEvent.getId(),
                temperatureEvent.getHubId(),
                temperatureEvent.getTimestamp(),
                temperatureEvent.getTemperatureC(),
                temperatureEvent.getTemperatureF()
        );
    }
}
