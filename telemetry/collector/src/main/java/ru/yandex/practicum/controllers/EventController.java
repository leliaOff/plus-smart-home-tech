package ru.yandex.practicum.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.exceptions.HubHandlerNotFound;
import ru.yandex.practicum.exceptions.SensorHandlerNotFound;
import ru.yandex.practicum.models.hub.HubEvent;
import ru.yandex.practicum.models.hub.enums.HubEventType;
import ru.yandex.practicum.models.sensor.SensorEvent;
import ru.yandex.practicum.models.sensor.enums.SensorEventType;
import ru.yandex.practicum.sources.kafka.handlers.hub.HubEventHandler;
import ru.yandex.practicum.sources.kafka.handlers.sensor.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public EventController(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void sensors(@Valid @RequestBody SensorEvent event) {
        SensorEventHandler handler = sensorEventHandlers.get(event.getType());
        if (handler == null) {
            throw new SensorHandlerNotFound("Не найдено событие сенсора: " + event.getType());
        }
        handler.handle(event);
    }

    @PostMapping("/hubs")
    public void hubs(@Valid @RequestBody HubEvent event) {
        HubEventHandler handler = hubEventHandlers.get(event.getType());
        if (handler == null) {
            throw new HubHandlerNotFound("Не не найдено событие хаба: " + event.getType());
        }
        handler.handle(event);
    }
}
