package ru.lena.restaurant.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import ru.lena.restaurant.model.*;
import ru.lena.restaurant.to.DishTo;
import ru.lena.restaurant.to.RestaurantTo;
import ru.lena.restaurant.to.UserTo;
import ru.lena.restaurant.to.VoteHistoryTo;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityUtil {

    private EntityUtil() {
    }

    public static Dish createNewFromTo(DishTo dishTo) {
        return new Dish( dishTo.getName(), dishTo.getPrice(), dishTo.isTodayMenuDish());
    }

    public static DishTo asTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), dish.isTodayMenuDish());
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(null, restaurantTo.getName(), restaurantTo.getScore(), null);
    }

    public static RestaurantTo asTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getScore(), restaurant.getAllDishPrice());
    }

    public static VoteHistoryTo asTo(VoteHistory voteHistory) {
        VoteHistoryTo to = new VoteHistoryTo(voteHistory.getId(), voteHistory.getRestaurant().getId(),voteHistory.getScore(),voteHistory.getVotTime());
        to.setMenuOfDay(voteHistory.getMenuOfDay()
                .stream()
                .map(EntityUtil::asTo)
                .collect(Collectors.toList()));
        return to;
    }

    public static List<VoteHistoryTo> asToList(Collection<VoteHistory> voteHistories) {
        return voteHistories.stream()
                .map(EntityUtil::asTo)
                .collect(Collectors.toList());
    }

    public static List<DishTo> asToListDishes(Collection<Dish> dishes) {
        return dishes.stream()
                .map(EntityUtil::asTo)
                .collect(Collectors.toList());
    }

    public static List<RestaurantTo> asToListRest(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(EntityUtil::asTo)
                .collect(Collectors.toList());
    }
    public static List<UserTo> asToListUsers(Collection<User> users) {
        return users.stream()
                .map(EntityUtil::asTo)
                .collect(Collectors.toList());
    }

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.ROLE_USER);
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }

}
