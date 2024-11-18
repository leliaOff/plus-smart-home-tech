package ru.yandex.practicum.sources.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.models.hub.HubEvent;

@Slf4j
@RequiredArgsConstructor
public abstract class HubEventHandler<T extends SpecificRecordBase> implements HubEventHandlerInterface {
    protected final Config.KafkaEventProducer producer;
    protected final Config topics;

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event) {
        T avroEvent = mapToAvro(event);
        String topic = topics.producer.getTopics().get(Config.TopicType.HUBS_EVENTS);
        log.info("Событие хаба {}. Топик {}", getMessageType(), topic);
        producer.send(topic, event.getHubId(), avroEvent);
    }
}
