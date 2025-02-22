package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "order-service", path = "/api/v1/order")
public interface OrderClient {
    @PostMapping("/completed")
    OrderDto completed(@RequestParam UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestParam UUID orderId);
}
