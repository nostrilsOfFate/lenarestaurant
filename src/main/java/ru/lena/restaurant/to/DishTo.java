package ru.lena.restaurant.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class DishTo extends BaseTo implements Serializable {

    @NotBlank
    @Range(min = 2, max = 100)
    @SafeHtml
    private String name;

    @NotBlank
    @SafeHtml
    @Range(min = 0)
    private BigDecimal price;

    private boolean todayMenuDish;

    public DishTo(Long id, String name, BigDecimal price, boolean todayMenuDish) {
        super(id);
        this.name = name;
        this.todayMenuDish = todayMenuDish;
        this.price = price;
    }
}
