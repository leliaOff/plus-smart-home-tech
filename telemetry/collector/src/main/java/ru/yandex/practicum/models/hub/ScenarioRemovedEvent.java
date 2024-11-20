package ru.yandex.practicum.models.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.models.hub.enums.HubEventType;

@Getter
@Setter
@ToString
public class ScenarioRemovedEvent extends HubEvent {
    @NotBlank
    @Size(min = 3)
    private String name;

    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
