package ru.yandex.practicum.services.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.models.Address;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AddressMapper {
    @Mapping(target = "addressId", ignore = true)
    Address fromAddressDto(AddressDto addressDto);

    AddressDto toAddressDto(Address address);
}