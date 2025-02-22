package ru.yandex.practicum.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.enums.PaymentState;
import ru.yandex.practicum.exceptions.PaymentNotFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.models.Payment;
import ru.yandex.practicum.repositories.PaymentRepository;
import ru.yandex.practicum.services.mappers.PaymentMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    @Transactional
    public PaymentDto payment(OrderDto orderDto) {
        log.info("Формирование оплаты для заказа: {}", orderDto.getOrderId());
        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .productTotal(productCost(orderDto))
                .deliveryTotal(orderDto.getDeliveryPrice())
                .totalPayment(totalCost(orderDto))
                .state(PaymentState.PENDING)
                .build();
        repository.save(payment);
        return mapper.toPaymentDto(payment);
    }

    public BigDecimal totalCost(OrderDto orderDto) {
        log.info("Расчёт полной стоимости заказа: {}", orderDto.getOrderId());
        BigDecimal productTotal = productCost(orderDto);
        BigDecimal deliveryPrice = orderDto.getDeliveryPrice();
        BigDecimal vat = productTotal.multiply(BigDecimal.valueOf(0.1));
        return productTotal.add(vat).add(deliveryPrice);
    }

    @Transactional
    public void refund(UUID paymentId) {
        log.info("Эмуляция успешной оплаты в платежного шлюза: {}", paymentId);
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Оплата не найдена " + paymentId));
        payment.setState(PaymentState.SUCCESS);
        repository.save(payment);
        orderClient.completed(payment.getOrderId());
    }

    @Transactional
    public BigDecimal productCost(OrderDto orderDto) {
        log.info("Расчёт стоимости товаров в заказе: {}", orderDto.getOrderId());
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<UUID, Integer> entry : orderDto.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();
            BigDecimal productPrice = shoppingStoreClient.getProduct(productId).getPrice();
            BigDecimal lineTotal = productPrice.multiply(BigDecimal.valueOf(quantity));
            total = total.add(lineTotal);
        }
        return total;
    }

    @Transactional
    public void failed(UUID paymentId) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Оплата не найдена " + paymentId));
        payment.setState(PaymentState.FAILED);
        repository.save(payment);
        orderClient.paymentFailed(payment.getOrderId());
    }
}
