package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.math.BigDecimal;

@FeignClient(name = "payment-service", path = "/api/v1/payment")
public interface PaymentClient {
    @PostMapping("/productCost")
    BigDecimal productCost(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    BigDecimal totalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    PaymentDto payment(@RequestBody OrderDto orderDto);
}
