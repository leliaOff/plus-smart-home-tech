package ru.yandex.practicum.models.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.models.hub.enums.DeviceActionType;

@Getter
@Setter
@ToString
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private int value;
}
