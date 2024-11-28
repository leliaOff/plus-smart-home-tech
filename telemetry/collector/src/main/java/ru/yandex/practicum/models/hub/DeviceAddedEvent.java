package ru.yandex.practicum.models.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.models.hub.enums.DeviceType;
import ru.yandex.practicum.models.hub.enums.HubEventType;

@Getter
@Setter
@ToString
public class DeviceAddedEvent extends HubEvent {
    @NotBlank
    private String id;
    @NotBlank
    private DeviceType deviceType;

    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
