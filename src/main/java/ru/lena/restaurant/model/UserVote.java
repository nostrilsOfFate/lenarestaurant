package ru.lena.restaurant.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_votes")
public class UserVote extends AbstractBaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "restaurant_id")
    private Long restaurantId;
}
