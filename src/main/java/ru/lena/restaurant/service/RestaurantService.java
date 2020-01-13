package ru.lena.restaurant.service;

import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.to.RestaurantTo;

import java.util.List;

public interface RestaurantService {
    Restaurant create(RestaurantTo restaurantTo);
    void delete(long id);
    List<Restaurant> getAll();
    Restaurant get(long id);
    void update(RestaurantTo restaurantTo);
    void update(Restaurant restaurant);
    List<Restaurant> findAllSortedByName();
    Restaurant getByName(String name);
    void voteForRestaurant(long userId, long restaurantId);
}
