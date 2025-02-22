package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.services.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService service;

    @PostMapping
    public PaymentDto payment(@RequestBody OrderDto orderDto) {
        return service.payment(orderDto);
    }

    @PostMapping("/totalCost")
    public BigDecimal totalCost(@RequestBody OrderDto orderDto) {
        return service.totalCost(orderDto);
    }

    @PostMapping("/refund")
    public void refund(@RequestParam UUID paymentId) {
        service.refund(paymentId);
    }

    @PostMapping("/productCost")
    public BigDecimal productCost(@RequestBody OrderDto orderDto) {
        return service.productCost(orderDto);
    }

    @PostMapping("/failed")
    public void failed(@RequestParam UUID paymentId) {
        service.failed(paymentId);
    }
}
