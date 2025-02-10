package ru.yandex.practicum.services.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.models.WarehouseProduct;
import ru.yandex.practicum.requests.NewProductInWarehouseRequest;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface WarehouseMapper {
    WarehouseProduct toWarehouseProduct(NewProductInWarehouseRequest request);
}
