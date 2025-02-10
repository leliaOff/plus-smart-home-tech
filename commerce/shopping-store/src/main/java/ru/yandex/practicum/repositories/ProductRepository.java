package ru.yandex.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.models.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT product FROM Product product WHERE product.productCategory = :category")
    Page<Product> findAllByProductCategory(@Param("category") ProductCategory category, Pageable pageable);
}
