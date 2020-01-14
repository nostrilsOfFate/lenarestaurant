package ru.lena.restaurant.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import ru.lena.restaurant.model.Dish;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class RestaurantTo extends BaseTo implements Serializable {

    @NotBlank
    @Size(min = 1, max = 100)
    @SafeHtml
    private String name;

    @SafeHtml
    @NotBlank
    private Integer score = 0;

    @NotBlank
    @SafeHtml
    private Set<DishTo> menu = new HashSet<>();

    @Range(min = 0)
    @NotBlank
    @SafeHtml
    BigDecimal allDishPrice = BigDecimal.ZERO;


    public RestaurantTo(String name, Integer score, BigDecimal allDishPrice, Set<DishTo> menu) {
        this.name = name;
        this.score = score;
        this.allDishPrice = allDishPrice;
        this.menu = menu;
    }

    @ConstructorProperties({"id", "name", "score", "allDishPrice"})
    public RestaurantTo(Long id, String name, Integer score, BigDecimal allDishPrice) {
        super(id);
        this.name = name;
        this.score = score;
        this.allDishPrice = allDishPrice;
    }
}
