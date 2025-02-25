package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.requests.AssemblyProductsRequest;
import ru.yandex.practicum.requests.ShippedToDeliveryRequest;

@FeignClient(name = "warehouse-service", path = "/api/v1/warehouse")
public interface WarehouseClient {

    @PostMapping("/assembly")
    BookedProductsDto assembly(@RequestBody AssemblyProductsRequest request);

    @GetMapping("/address")
    AddressDto address();

    @PostMapping("shipped")
    void shipped(@RequestBody ShippedToDeliveryRequest request);
}
