package ru.yandex.practicum.models.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.models.hub.enums.HubEventType;

@Getter
@Setter
@ToString
public class DeviceRemovedEvent extends HubEvent {
    @NotBlank
    private String id;

    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
