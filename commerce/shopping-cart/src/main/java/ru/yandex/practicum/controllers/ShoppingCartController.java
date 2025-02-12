package ru.yandex.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.requests.ChangeProductQuantityRequest;
import ru.yandex.practicum.services.ShoppingCartService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService service;

    @GetMapping
    public ShoppingCartDto get(@RequestParam String username) {
        return service.getOrCreate(username);
    }

    @PutMapping
    public ShoppingCartDto add(@RequestParam String username, @RequestBody Map<UUID, Integer> products) {
        return service.add(username, products);
    }

    @DeleteMapping
    public void deactivate(@RequestParam String username) {
        service.deactivate(username);
    }


    @PostMapping("/remove")
    public ShoppingCartDto remove(@RequestParam String username, @RequestBody Map<UUID, Integer> products) {
        return service.remove(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeQuantity(@RequestParam String username, @Valid @RequestBody ChangeProductQuantityRequest request) {
        return service.changeQuantity(username, request);
    }
}
