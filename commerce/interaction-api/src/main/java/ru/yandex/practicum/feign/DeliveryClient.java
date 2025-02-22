package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.math.BigDecimal;

@FeignClient(name = "delivery-service", path = "/api/v1/delivery")
public interface DeliveryClient {
    @PutMapping
    DeliveryDto add(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/cost")
    BigDecimal cost(@RequestBody OrderDto orderDto);
}
