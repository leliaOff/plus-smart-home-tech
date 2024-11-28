package ru.yandex.practicum.models.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.models.hub.enums.ScenarioConditionOperation;
import ru.yandex.practicum.models.hub.enums.ScenarioConditionType;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    private String sensorId;
    private ScenarioConditionType type;
    private ScenarioConditionOperation operation;
    private int value;
}
