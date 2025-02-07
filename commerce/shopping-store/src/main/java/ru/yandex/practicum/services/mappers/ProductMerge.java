package ru.yandex.practicum.services.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.models.Product;

import java.math.BigDecimal;

public class ProductMerge {
    public static Product mergeProductDto(Product product, ProductDto productDto)
    {
        if (productDto.getProductName() != null) {
            product.setProductName(productDto.getProductName());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getImageSrc() != null) {
            product.setImageSrc(productDto.getImageSrc());
        }
        if (productDto.getQuantityState() != null) {
            product.setQuantityState(productDto.getQuantityState());
        }
        if (productDto.getProductState() != null) {
            product.setProductState(productDto.getProductState());
        }
        if (productDto.getRating() > 0) {
            product.setRating(productDto.getRating());
        }
        if (productDto.getProductCategory() != null) {
            product.setProductCategory(productDto.getProductCategory());
        }
        if (productDto.getPrice() != null && productDto.getPrice().compareTo(BigDecimal.ONE) >= 0) {
            product.setPrice(productDto.getPrice());
        }
        return product;
    }
}
