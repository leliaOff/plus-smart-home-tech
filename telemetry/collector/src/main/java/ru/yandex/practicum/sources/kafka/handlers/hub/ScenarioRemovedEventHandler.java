package ru.yandex.practicum.sources.kafka.handlers.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.models.hub.HubEvent;
import ru.yandex.practicum.models.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.models.hub.enums.HubEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class ScenarioRemovedEventHandler extends HubEventHandler<ScenarioRemovedEventAvro> {
    public ScenarioRemovedEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        var scenarioRemovedEvent = (ScenarioRemovedEvent) event;
        return new ScenarioRemovedEventAvro(
                scenarioRemovedEvent.getName()
        );
    }
}
