package ru.yandex.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.exceptions.NoOrderFoundException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.repositories.AddressRepository;
import ru.yandex.practicum.repositories.OrderRepository;
import ru.yandex.practicum.requests.AssemblyProductsRequest;
import ru.yandex.practicum.requests.CreateNewOrderRequest;
import ru.yandex.practicum.requests.ProductReturnRequest;
import ru.yandex.practicum.services.mappers.AddressMapper;
import ru.yandex.practicum.services.mappers.OrderMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final OrderValidator validator;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    public List<OrderDto> get(String username) {
        log.info("Получить заказы пользователя: {}", username);
        validator.validateUsername(username);

        List<Order> orders = orderRepository.findByUsername(username);

        return orders.stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto create(CreateNewOrderRequest request) {
        log.info("Создать новый заказ в системе");

        UUID shoppingCartId = request.getShoppingCart().getShoppingCartId();

        Order order = Order.builder()
                .shoppingCartId(shoppingCartId)
                .username(request.getUsername())
                .products(request.getShoppingCart().getProducts())
                .state(OrderState.NEW)
                .build();

        Order savedOrder = orderRepository.save(order);

        BookedProductsDto bookedProduct = warehouseClient.assembly(
                new AssemblyProductsRequest(savedOrder.getOrderId(), shoppingCartId)
        );

        savedOrder.setDeliveryWeight(bookedProduct.getDeliveryWeight());
        savedOrder.setDeliveryVolume(bookedProduct.getDeliveryVolume());
        savedOrder.setFragile(bookedProduct.getFragile());
        savedOrder.setFromAddress(null);
        savedOrder.setToAddress(addressMapper.fromAddressDto(request.getDeliveryAddress()));

        orderRepository.save(order);

        AddressDto fromAddress = warehouseClient.address();
        order.setFromAddress(addressMapper.fromAddressDto(fromAddress));

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .deliveryId(UUID.randomUUID())
                .orderId(order.getOrderId())
                .fromAddress(fromAddress)
                .toAddress(addressMapper.toAddressDto(order.getToAddress()))
                .state(DeliveryState.CREATED)
                .build();

        DeliveryDto createdDelivery = deliveryClient.add(deliveryDto);
        order.setDeliveryId(createdDelivery.getDeliveryId());

        BigDecimal productPrice = paymentClient.productCost(orderMapper.toOrderDto(order));
        order.setProductPrice(productPrice);

        BigDecimal deliveryPrice = deliveryClient.cost(orderMapper.toOrderDto(order));
        order.setDeliveryPrice(deliveryPrice);

        BigDecimal totalPrice = paymentClient.totalCost(orderMapper.toOrderDto(order));
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto productReturn(ProductReturnRequest request) {
        log.info("Возврат заказа: {}", request.getOrderId());
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + request.getOrderId()));

        order.setState(OrderState.PRODUCT_RETURNED);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto payment(UUID orderId) {
        log.info("Оплата заказа: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        PaymentDto paymentDto = paymentClient.payment(orderMapper.toOrderDto(order));
        order.setPaymentId(paymentDto.getPaymentId());
        order.setState(OrderState.PAID);

        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto paymentFailed(UUID orderId) {
        log.info("Оплата заказа {} произошла с ошибкой", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        order.setState(OrderState.PAYMENT_FAILED);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto delivery(UUID orderId) {
        log.info("Доставка заказа: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        order.setState(OrderState.DELIVERED);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto deliveryFailed(UUID orderId) {
        log.info("Доставка заказа {} произошла с ошибкой", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        order.setState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto completed(UUID orderId) {
        log.info("Завершение заказа: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        order.setState(OrderState.COMPLETED);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto calculateTotal(UUID orderId) {
        log.info("Расчёт стоимости заказа: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        BigDecimal totalPrice = paymentClient.totalCost(orderMapper.toOrderDto(order));
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto calculateDelivery(UUID orderId) {
        log.info("Расчёт стоимости доставки заказа: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        BigDecimal deliveryPrice = deliveryClient.cost(orderMapper.toOrderDto(order));
        order.setDeliveryPrice(deliveryPrice);

        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto assembly(UUID orderId) {
        log.info("Сборка заказа: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        order.setState(OrderState.ASSEMBLED);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderDto assemblyFailed(UUID orderId) {
        log.info("Сборка заказа {} произошла с ошибкой", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ не найден: " + orderId));

        order.setState(OrderState.ASSEMBLY_FAILED);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }
}
