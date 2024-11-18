package ru.yandex.practicum.sources.kafka.handlers.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.models.sensor.ClimateSensorEvent;
import ru.yandex.practicum.models.sensor.SensorEvent;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class ClimateSensorEventHandler extends SensorEventHandler<ClimateSensorAvro> {
    public ClimateSensorEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        var climateEvent = (ClimateSensorEvent) event;
        return new ClimateSensorAvro(
                climateEvent.getTemperatureC(),
                climateEvent.getHumidity(),
                climateEvent.getCo2Level()
        );
    }
}
