package ru.yandex.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.models.WarehouseProduct;
import ru.yandex.practicum.repositories.BookingRepository;
import ru.yandex.practicum.repositories.WarehouseRepository;
import ru.yandex.practicum.requests.AddProductToWarehouseRequest;
import ru.yandex.practicum.requests.NewProductInWarehouseRequest;
import ru.yandex.practicum.requests.SetProductQuantityStateRequest;
import ru.yandex.practicum.services.mappers.BookingMapper;
import ru.yandex.practicum.services.mappers.QuantityStateMapper;
import ru.yandex.practicum.services.mappers.WarehouseMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.yandex.practicum.dto.AddressDto.getDefaultAddress;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final BookingRepository bookingRepository;
    private final WarehouseRepository warehouseRepository;
    private final ShoppingStoreClient client;
    private final WarehouseMapper warehouseMapper;
    private final BookingMapper bookingMapper;

    public AddressDto getAddress() {
        log.info("Предоставить адрес склада для расчёта доставки");
        return getDefaultAddress();
    }

    @Transactional
    public void create(NewProductInWarehouseRequest request) {
        log.info("Добавить новый товар на склад: {}", request);
        if (warehouseRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Данный товар уже существует");
        }
        ProductDto productDto = client.getProduct(request.getProductId());
        if (productDto == null) {
            throw new RuntimeException("Товар не найден");
        }
        WarehouseProduct newProduct = warehouseMapper.toWarehouseProduct(request);
        newProduct.setQuantityAvailable(0);
        warehouseRepository.save(newProduct);
    }

    @Transactional
    public void add(AddProductToWarehouseRequest request) {
        log.info("Принять товар на склад: {}", request);

        WarehouseProduct product = warehouseRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товар не найден"));

        product.setQuantityAvailable(product.getQuantityAvailable() + request.getQuantity());
        WarehouseProduct updatedProduct = warehouseRepository.save(product);

        QuantityState newState = QuantityStateMapper.toQuantityState(updatedProduct.getQuantityAvailable());
        client.setProductQuantityState(
                SetProductQuantityStateRequest.builder()
                        .productId(request.getProductId())
                        .quantityState(newState)
                        .build()
        );
    }

    public BookedProductsDto check(ShoppingCartDto request) {
        log.info("Предварительно проверить что количество товаров на складе достаточно для данной корзины продуктов {}", request);

        Map<UUID, Integer> products = request.getProducts();
        List<WarehouseProduct> warehouseProducts = warehouseRepository.findAllById(products.keySet());
        warehouseProducts.forEach(warehouseProduct -> {
            if (warehouseProduct.getQuantityAvailable() < products.get(warehouseProduct.getProductId()))
                throw new ProductInShoppingCartLowQuantityInWarehouse("Товара не достаточно");
        });

        double deliveryWeight = warehouseProducts.stream()
                .map(WarehouseProduct::getWeight)
                .mapToDouble(Double::doubleValue)
                .sum();

        double deliveryVolume = warehouseProducts.stream()
                .map(warehouseProduct -> warehouseProduct.getDimension().getDepth()
                        * warehouseProduct.getDimension().getHeight() * warehouseProduct.getDimension().getWidth())
                .mapToDouble(Double::doubleValue)
                .sum();

        boolean fragile = warehouseProducts.stream()
                .anyMatch(WarehouseProduct::isFragile);
        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }
}
