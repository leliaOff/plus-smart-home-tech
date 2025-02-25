package ru.yandex.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.exceptions.NoDeliveryFoundException;
import ru.yandex.practicum.exceptions.NoOrderFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.models.Delivery;
import ru.yandex.practicum.repositories.DeliveryRepository;
import ru.yandex.practicum.requests.ShippedToDeliveryRequest;
import ru.yandex.practicum.services.mappers.DeliveryMapper;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository repository;
    private final DeliveryMapper mapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Transactional
    public DeliveryDto create(DeliveryDto deliveryDto) {
        log.info("Создать новую доставку: {}", deliveryDto);
        deliveryDto.setState(DeliveryState.CREATED);
        Delivery delivery = mapper.fromDeliveryDto(deliveryDto);
        repository.save(delivery);
        return mapper.toDeliveryDto(delivery);
    }

    @Transactional
    public void successful(UUID orderId) {
        log.info("Эмуляция успешной доставки товара: {}", orderId);
        Delivery delivery = repository.findByOrderId(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));
        delivery.setState(DeliveryState.DELIVERED);
        repository.save(delivery);
        orderClient.delivery(delivery.getOrderId());
    }

    @Transactional(readOnly = true)
    public void picked(UUID deliveryId) {
        log.info("Эмуляция получения товара в доставку: {}", deliveryId);
        Delivery delivery = repository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка не найдена: " + deliveryId));
        delivery.setState(DeliveryState.IN_DELIVERY);
        repository.save(delivery);
        ShippedToDeliveryRequest request = ShippedToDeliveryRequest.builder()
                .orderId(delivery.getOrderId())
                .deliveryId(deliveryId)
                .build();
        warehouseClient.shipped(request);
    }

    // Эмуляция неудачного вручения товара.
    @Transactional(readOnly = true)
    public void failed(UUID orderId) {
        log.info("Эмуляция неудачного вручения товара: {}", orderId);
        Delivery delivery = repository.findByOrderId(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));
        delivery.setState(DeliveryState.FAILED);
        repository.save(delivery);
        orderClient.deliveryFailed(orderId);
    }

    @Transactional
    public BigDecimal cost(OrderDto orderDto) {
        log.info("Расчёт полной стоимости доставки заказа: {}", orderDto);
        Delivery delivery = repository.findById(orderDto.getDeliveryId())
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка не найдена: " + orderDto.getDeliveryId()));
        delivery.setDeliveryWeight(orderDto.getDeliveryWeight());
        delivery.setDeliveryVolume(orderDto.getDeliveryVolume());
        delivery.setFragile(orderDto.isFragile());
        repository.save(delivery);

        String warehouseAddress = String.valueOf(warehouseClient.address());

        final BigDecimal BASE_RATE = BigDecimal.valueOf(5.0);
        BigDecimal step1 = DeliveryCostService.getCost(warehouseAddress, BASE_RATE);

        BigDecimal fragileAddition = orderDto.isFragile() ? step1.multiply(BigDecimal.valueOf(0.2)) : BigDecimal.ZERO;
        BigDecimal step2 = step1.add(fragileAddition);

        BigDecimal weightAddition = BigDecimal.valueOf(orderDto.getDeliveryWeight()).multiply(BigDecimal.valueOf(0.3));
        BigDecimal step3 = step2.add(weightAddition);

        BigDecimal volumeAddition = BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(BigDecimal.valueOf(0.2));
        BigDecimal step4 = step3.add(volumeAddition);

        String deliveryStreet = delivery.getToAddress().getStreet();
        BigDecimal addressAddition = warehouseAddress.equals(deliveryStreet)
                ? BigDecimal.ZERO
                : step4.multiply(BigDecimal.valueOf(0.2));
        return step4.add(addressAddition);
    }
}
