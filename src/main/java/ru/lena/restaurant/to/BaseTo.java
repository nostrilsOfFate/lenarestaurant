package ru.lena.restaurant.to;

import lombok.*;
import ru.lena.restaurant.HasId;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseTo implements HasId {
    protected Long id;
}
