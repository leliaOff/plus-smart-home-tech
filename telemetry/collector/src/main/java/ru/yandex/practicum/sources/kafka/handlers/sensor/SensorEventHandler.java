package ru.yandex.practicum.sources.kafka.handlers.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.sources.kafka.Config;

@Slf4j
@RequiredArgsConstructor
public abstract class SensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandlerInterface {
    protected final Config.KafkaEventProducer producer;
    protected final Config topics;

    protected abstract T mapToAvro(SensorEventProto event);

    @Override
    public void handle(SensorEventProto event) {
        T protoEvent = mapToAvro(event);
        String topic = topics.getProducer().getTopics().get(Config.TopicType.SENSORS_EVENTS);
        log.info("Событие датчика {}. Топик {}", getMessageType(), topic);
        producer.send(topic, event.getId(), protoEvent);
    }
}
