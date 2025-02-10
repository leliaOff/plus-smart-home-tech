package ru.yandex.practicum.models;


import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Dimension {
    private double width;
    private double height;
    private double depth;
}
