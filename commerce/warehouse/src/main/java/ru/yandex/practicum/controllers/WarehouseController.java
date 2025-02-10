package ru.yandex.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.requests.AddProductToWarehouseRequest;
import ru.yandex.practicum.requests.NewProductInWarehouseRequest;
import ru.yandex.practicum.services.WarehouseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final WarehouseService service;

    @GetMapping("/address")
    public AddressDto getAddress() {
        return service.getAddress();
    }

    @PutMapping
    public void create(@RequestBody @Valid NewProductInWarehouseRequest request) {
        service.create(request);
    }

    @PostMapping("/add")
    public void add(@RequestBody @Valid AddProductToWarehouseRequest request) {
        service.add(request);
    }

    @PostMapping("/check")
    public BookedProductsDto check(@RequestBody @Valid ShoppingCartDto request) {
        return service.check(request);
    }
}
