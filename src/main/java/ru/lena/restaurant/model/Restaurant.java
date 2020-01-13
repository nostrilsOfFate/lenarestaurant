package ru.lena.restaurant.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Access(AccessType.FIELD)
@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Range(min = 0)
    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @OneToMany(orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<Dish> menu = new HashSet<>();

    @NotNull
    @Range(min = 0)
    @Column(name = "all_price_dish", nullable = false)
    private BigDecimal allDishPrice = BigDecimal.ZERO;

    public Restaurant(Long id, String name, Integer score, Set<Dish> menu) {
        super(id);
        this.name = name;
        this.score = score;
        this.menu = menu;
    }

    public Restaurant(Long id, String name) {
        super(id);
        this.name = name;
    }
}
