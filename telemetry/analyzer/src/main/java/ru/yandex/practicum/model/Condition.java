package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "conditions")
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConditionType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConditionOperation operation;

    private Integer value;
}
