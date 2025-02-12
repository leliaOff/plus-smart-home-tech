package ru.yandex.practicum.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    @Column(nullable = false)
    private UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "booking_products", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;

    @Column(nullable = false)
    private Double deliveryWeight;

    @Column(nullable = false)
    private Double deliveryVolume;

    @Column(nullable = false)
    private Boolean fragile;
}
