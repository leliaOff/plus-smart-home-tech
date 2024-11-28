package ru.yandex.practicum.sources.kafka.handlers.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.models.hub.HubEvent;
import ru.yandex.practicum.sources.kafka.Config;

@Slf4j
@RequiredArgsConstructor
public abstract class HubEventHandler<T extends SpecificRecordBase> implements HubEventHandlerInterface {
    protected final Config.KafkaEventProducer producer;
    protected final Config topics;

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event) {
        T avroEvent = mapToAvro(event);
        String topic = topics.getProducer().getTopics().get(Config.TopicType.HUBS_EVENTS);
        log.info("Событие хаба {}. Топик {}", getMessageType(), topic);
        producer.send(topic, event.getHubId(), avroEvent);
    }
}
