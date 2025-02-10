package ru.yandex.practicum.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.PageableDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.models.Product;
import ru.yandex.practicum.repositories.ProductRepository;
import ru.yandex.practicum.requests.SetProductQuantityStateRequest;
import ru.yandex.practicum.services.mappers.PageableMapper;
import ru.yandex.practicum.services.mappers.ProductMapper;
import ru.yandex.practicum.services.mappers.ProductMerge;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public List<ProductDto> getList(ProductCategory category, PageableDto pageableDto) {
        log.info("Получение списка товаров по типу в пагинированном виде: {}", category);
        Pageable pageable = PageableMapper.toPageable(pageableDto);
        List<Product> products = repository.findAllByProductCategory(category, pageable).getContent();
        return products.stream()
                .map(mapper::toProductDto)
                .collect(Collectors.toList());
    }

    public ProductDto get(UUID productId) {
        log.info("Получить сведения по товару из БД: {}", productId);
        Product product = repository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Товар не найден: {}", productId);
                    return new ProductNotFoundException("Товар не найден");
                });
        return mapper.toProductDto(product);
    }

    @Transactional
    public ProductDto create(ProductDto productDto) {
        log.info("Создание нового товара в ассортименте: {}", productDto);
        Product product = mapper.toProduct(productDto);
        Product savedProduct = repository.save(product);
        return mapper.toProductDto(savedProduct);
    }

    @Transactional
    public ProductDto update(ProductDto productDto) {
        UUID productId = productDto.getProductId();
        log.info("Обновление товара в ассортименте: {}", productId);
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));
        Product updatedProduct = repository.save(ProductMerge.mergeProductDto(product, productDto));
        return mapper.toProductDto(updatedProduct);
    }

    @Transactional
    public Boolean removeProductFromStore(UUID productId) {
        log.info("Удалить товар из ассортимента магазина: {}", productId);
        Product product = repository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Товар не найден: {}", productId);
                    return new ProductNotFoundException("Товар не найден");
                });
        repository.delete(product);
        return true;
    }

    @Transactional
    public Boolean quantityState(SetProductQuantityStateRequest request) {
        UUID productId = request.getProductId();
        log.info("Установка статуса по товару: {}, статус: {}", productId, request.getQuantityState());
        Product product = repository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Товар не найден: {}", productId);
                    return new ProductNotFoundException("Товар не найден");
                });
        product.setQuantityState(request.getQuantityState());
        repository.save(product);
        return true;
    }
}
