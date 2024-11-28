package ru.yandex.practicum.models.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.models.hub.enums.HubEventType;

import java.util.List;

@Getter
@Setter
@ToString
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    @Size(min = 3, max = 2147483647)
    private String name;
    @NotBlank
    private List<ScenarioCondition> conditions;
    @NotBlank
    private List<DeviceAction> actions;

    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
