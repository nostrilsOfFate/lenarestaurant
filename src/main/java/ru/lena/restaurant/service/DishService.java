package ru.lena.restaurant.service;

import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.to.DishTo;

import java.util.List;

public interface DishService {
    Dish create(DishTo dishTo);

    void delete(long id);
    List<Dish> getAll();
    Dish get(long id);
    void update(DishTo dishTo);
    void update(Dish dish);
    void setTodayMenu(Long dishId, boolean isDishToday);
}
