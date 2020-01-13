package ru.lena.restaurant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import ru.lena.restaurant.utils.DateTimeUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vote_history")
public class VoteHistory extends AbstractBaseEntity {

    @ManyToOne
    @NotNull
    @JoinColumn(referencedColumnName = "ID")
    private Restaurant restaurant;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @NotNull
    private List<Dish> menuOfDay = new ArrayList<>();

    @Column(name = "rest_score", nullable = false)
    @NotNull
    @Range(min = 0)
    private Integer score = 0;

    @Column(name = "vot_time", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_PATTERN)
    private LocalDate votTime = LocalDate.now();


    public VoteHistory(Restaurant restaurant, List<Dish> menuOfDay, Integer score) {
        this.restaurant = restaurant;
        this.menuOfDay = menuOfDay;
        this.score = score;
    }

    public VoteHistory(Long id, Restaurant restaurant, List<Dish> menuOfDay, Integer score) {
        super(id);
        this.restaurant = restaurant;
        this.menuOfDay = menuOfDay;
        this.score = score;
    }
}
