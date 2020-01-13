package ru.lena.restaurant.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dishes")
public class Dish extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "today_menu_dish", nullable = false, columnDefinition = "boolean default false")
    private boolean todayMenuDish;

    public Dish(Long id, String name, BigDecimal price, boolean todayMenuDish) {
        super(id);
        this.name = name;
        this.price = price;
        this.todayMenuDish = todayMenuDish;
    }
}
