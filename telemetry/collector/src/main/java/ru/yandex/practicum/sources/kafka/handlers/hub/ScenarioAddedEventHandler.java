package ru.yandex.practicum.sources.kafka.handlers.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.models.hub.HubEvent;
import ru.yandex.practicum.models.hub.ScenarioAddedEvent;
import ru.yandex.practicum.models.hub.enums.HubEventType;
import ru.yandex.practicum.sources.kafka.Config;

@Service
public class ScenarioAddedEventHandler extends HubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(Config.KafkaEventProducer producer, Config kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        var scenarioAddedEvent = (ScenarioAddedEvent) event;
        return new ScenarioAddedEventAvro(
                scenarioAddedEvent.getName(),
                scenarioAddedEvent.getConditions().stream().map(condition -> new ScenarioConditionAvro(
                        condition.getSensorId(),
                        ConditionTypeAvro.valueOf(condition.getType().name()),
                        ConditionOperationAvro.valueOf(condition.getOperation().name()),
                        condition.getValue()
                )).toList(),
                scenarioAddedEvent.getActions().stream().map(action -> new DeviceActionAvro(
                        action.getSensorId(),
                        ActionTypeAvro.valueOf(action.getType().name()),
                        action.getValue()
                )).toList()
        );
    }
}
