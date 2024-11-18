package ru.yandex.practicum.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.models.hub.HubEvent;
import ru.yandex.practicum.models.sensor.SensorEvent;

@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {
    @PostMapping("/sensors")
    public void sensors(@Valid @RequestBody SensorEvent event) {

    }
    @PostMapping("/hubs")
    public void hubs(@Valid @RequestBody HubEvent event) {

    }
}
