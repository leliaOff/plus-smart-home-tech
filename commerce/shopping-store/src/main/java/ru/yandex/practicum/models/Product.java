package ru.yandex.practicum.models;

import enums.ProductCategory;
import enums.ProductState;
import enums.QuantityState;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String description;

    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductState productState;

    @Column(nullable = false)
    private double rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory productCategory;

    @Column(nullable = false)
    private BigDecimal price;
}
