package ru.yandex.practicum.services.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.models.Delivery;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DeliveryMapper {
    @Mapping(source = "fromAddress", target = "fromAddress")
    @Mapping(source = "toAddress", target = "toAddress")
    DeliveryDto toDeliveryDto(final Delivery delivery);

    @Mapping(target = "fragile", ignore = true)
    @Mapping(target = "deliveryWeight", ignore = true)
    @Mapping(target = "deliveryVolume", ignore = true)
    @Mapping(source = "fromAddress", target = "fromAddress")
    @Mapping(source = "toAddress", target = "toAddress")
    Delivery fromDeliveryDto(final DeliveryDto deliveryDto);
}