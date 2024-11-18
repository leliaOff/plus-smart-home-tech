package ru.yandex.practicum.sources.kafka.handlers.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.models.sensor.MotionSensorEvent;
import ru.yandex.practicum.models.sensor.SensorEvent;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class MotionSensorEventHandler extends SensorEventHandler<MotionSensorAvro> {
    public MotionSensorEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEvent event) {
        var motionEvent = (MotionSensorEvent) event;
        return new MotionSensorAvro(
                motionEvent.getLinkQuality(),
                motionEvent.isMotion(),
                motionEvent.getVoltage()
        );
    }
}
