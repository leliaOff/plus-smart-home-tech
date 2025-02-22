package ru.yandex.practicum.services.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.models.Payment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PaymentMapper {
    @Mapping(target = "feePayment", ignore = true)
    @Mapping(target = "deliveryPayment", ignore = true)
    PaymentDto toPaymentDto(Payment payment);
}