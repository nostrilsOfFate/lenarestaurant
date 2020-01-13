package ru.lena.restaurant.to;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;
import ru.lena.restaurant.utils.DateTimeUtil;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VoteHistoryTo extends BaseTo {

    @SafeHtml
    @NotBlank
    private Long restaurant_id;

    @SafeHtml
    @NotBlank
    @Range(min = 0)
    private Integer score = 0;

    @NotBlank
    private List<DishTo> menuOfDay = new ArrayList<>();

    @NotBlank
    @SafeHtml
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_PATTERN)
    private LocalDate votTime = LocalDate.now();

    public VoteHistoryTo(Long id, Long restaurant_id, Integer score, LocalDate votTime) {
        super(id);
        this.restaurant_id = restaurant_id;
        this.score = score;
        this.votTime = votTime;
    }

}
