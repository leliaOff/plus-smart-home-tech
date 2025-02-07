package ru.yandex.practicum.controllers;

import enums.ProductCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PageableDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.requests.SetProductQuantityStateRequest;
import ru.yandex.practicum.services.ShoppingStoreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController {
    private final ShoppingStoreService service;

    @GetMapping
    List<ProductDto> getList(@RequestParam("category") ProductCategory category,
                             @Valid @RequestParam("pageableDto") PageableDto pageableDto) {
        return service.getList(category, pageableDto);

    }

    @GetMapping("/{productId}")
    ProductDto get(@PathVariable("productId") UUID productId) {
        return service.get(productId);
    }

    @PutMapping
    ProductDto create(@Valid @RequestBody ProductDto productDto) {
        return service.create(productDto);
    }

    @PostMapping
    ProductDto update(@Valid @RequestBody ProductDto productDto) {
        return service.update(productDto);
    }

    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestParam("productId") UUID productId) {
        return service.removeProductFromStore(productId);
    }

    @PostMapping("/quantityState")
    Boolean quantityState(@Valid @RequestBody SetProductQuantityStateRequest request) {
        return service.quantityState(request);
    }

}
