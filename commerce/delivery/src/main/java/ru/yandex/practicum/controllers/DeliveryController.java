package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.services.DeliveryService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class DeliveryController {
    private final DeliveryService service;

    @PutMapping
    public DeliveryDto create(@RequestBody DeliveryDto deliveryDto) {
        return service.create(deliveryDto);
    }

    @PostMapping("/successful")
    void successful(@RequestParam UUID orderId) {
        service.successful(orderId);
    }

    @PostMapping("/picked")
    void picked(@RequestParam UUID deliveryId) {
        service.picked(deliveryId);
    }

    @PostMapping("/failed")
    void failed(@RequestParam UUID orderId) {
        service.failed(orderId);
    }

    @PostMapping("/cost")
    void cost(@RequestBody OrderDto orderDto) {
        service.cost(orderDto);
    }
}
