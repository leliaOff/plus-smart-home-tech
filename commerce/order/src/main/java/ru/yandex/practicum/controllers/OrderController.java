package ru.yandex.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.requests.CreateNewOrderRequest;
import ru.yandex.practicum.requests.ProductReturnRequest;
import ru.yandex.practicum.services.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService service;

    @GetMapping
    public List<OrderDto> get(@RequestParam String username) {
        return service.get(username);
    }

    @PutMapping
    public OrderDto create(@Valid @RequestBody CreateNewOrderRequest request) {
        return service.create(request);
    }

    @PostMapping("/return")
    public OrderDto productReturn(@RequestBody ProductReturnRequest request) {
        return service.productReturn(request);
    }

    @PostMapping("/payment")
    public OrderDto payment(@RequestParam UUID orderId) {
        return service.payment(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestParam UUID orderId) {
        return service.paymentFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto delivery(@RequestParam UUID orderId) {
        return service.delivery(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestParam UUID orderId) {
        return service.deliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completed(@RequestParam UUID orderId) {
        return service.completed(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestParam UUID orderId) {
        return service.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestParam UUID orderId) {
        return service.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembly(@RequestParam UUID orderId) {
        return service.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestParam UUID orderId) {
        return service.assemblyFailed(orderId);
    }
}
