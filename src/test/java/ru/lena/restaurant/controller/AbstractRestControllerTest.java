package ru.lena.restaurant.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.model.Role;
import ru.lena.restaurant.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbstractRestControllerTest {

//
//    @BeforeEach
//    void init() {
//        User USER = new User();
//        USER.setName("User");
//        USER.setEmail("user@yandex.ru");
//        USER.setPassword("{noop}password");
//        USER.setRoles(List.of(Role.ROLE_USER));
//
//        User ADMIN = new User();
//        USER.setName("Admin");
//        USER.setEmail("admin@gmail.com");
//        USER.setPassword("{noop}admin");
//        USER.setRoles(List.of(Role.ROLE_USER, Role.ROLE_ADMIN));
//
//        Restaurant restaurant1 = new Restaurant();
//        restaurant1.setName("Бонжур се ля ви");
//        restaurant1.setAllDishPrice(BigDecimal.valueOf(200));
//        restaurant1.setScore(15);
//        Restaurant restaurant2 = new Restaurant();
//        restaurant1.setName("Новый элитный ресторан 5 звезд");
//        restaurant1.setAllDishPrice(BigDecimal.valueOf(1200));
//        restaurant1.setScore(555);
//        Restaurant restaurant3 = new Restaurant();
//        restaurant1.setName("Столовая");
//        restaurant1.setAllDishPrice(BigDecimal.valueOf(0));
//        restaurant1.setScore(225);
//
//        Dish dish1 = new Dish("компот", BigDecimal.valueOf(50), true);
//        Dish dish2 = new Dish("багет", BigDecimal.valueOf(50), true);
//        Dish dish3 = new Dish("круасан", BigDecimal.valueOf(50), true);
//        Dish dish4 = new Dish("краб", BigDecimal.valueOf(350), true);
//        Dish dish5 = new Dish("икра заморская - баклажанная", BigDecimal.valueOf(100), true);
//        Dish dish6 = new Dish("икра красная", BigDecimal.valueOf(500), true);
//        Dish dish7 = new Dish("икра черная", BigDecimal.valueOf(500), true);
//        Dish dish8 = new Dish("пирожок с мясом", BigDecimal.valueOf(25), true);
//        Dish dish9 = new Dish("пирожок с черникой", BigDecimal.valueOf(25), true);
//        Dish dish10 = new Dish("борщ", BigDecimal.valueOf(50), true);
//        Dish dish11 = new Dish("новый модный обед", BigDecimal.valueOf(200), true);
//        Dish dish12 = new Dish("блины", BigDecimal.valueOf(50), false);
//        ArrayList<Dish> dishes = new ArrayList<>();
//        dishes.addAll(Arrays.asList(dish1, dish2, dish3, dish4, dish5, dish6, dish7, dish8, dish9, dish10, dish11, dish12));
//
//        USER.
//
//        //insert into USER_VOTES(RESTAURANT_ID, USER_ID)
//        //VALUES (1,1),
//        //       (3,2);
//        //insert into RESTAURANTS_MENU(RESTAURANT_ID, MENU_ID)
//        //VALUES (1, 2),
//        //       (1, 3),
//        //       (1, 5),
//        //       (2, 6),
//        //       (2, 7),
//        //       (2, 11),
//        //       (3, 1),
//        //       (3, 8),
//        //       (3, 9),
//        //       (3, 10),
//        //       (3, 4);
//        //
//        //insert into VOTE_HISTORY(REST_SCORE, VOT_TIME, RESTAURANT_ID)
//        //VALUES (27, '2019-02-03', 1),
//        //       (33, '2019-02-03', 2),
//        //       (19, '2019-02-03', 3),
//        //       (19, '2019-02-04', 1),
//        //       (19, '2019-02-04', 2),
//        //       (19, '2019-02-04', 3),
//        //       (15, '2019-02-05', 1),
//        //       (44, '2019-02-05', 2),
//        //       (2, '2019-02-05', 3);
//        //
//        //
//        //insert into VOTE_HISTORY_MENU_OF_DAY(VOTE_HISTORY_ID, MENU_OF_DAY_ID)
//        //VALUES (1, 2),
//        //       (1, 3),
//        //       (1, 4),
//        //       (2, 11),
//        //       (2, 12),
//        //       (2, 6),
//        //       (2, 7),
//        //       (3, 1),
//        //       (3, 8),
//        //       (3, 9),
//        //       (3, 10),
//        //       (4, 3),
//        //       (4, 4),
//        //       (5, 6),
//        //       (5, 7),
//        //       (6, 1),
//        //       (6, 8),
//        //       (7, 2),
//        //       (7, 3),
//        //       (8, 11),
//        //       (8, 12),
//        //       (9, 9),
//        //       (9, 10);
//
//
//        dishes.addAll(Arrays.asList(dish1, dish2, dish3, dish4, dish5, dish6, dish7, dish8, dish9, dish10, dish11, dish12));
//        //dishes.forEach(operation -> operationDtos.add(toFinancialOperationDto(operation)));
//        Mockito.when(service.get(1L)).thenReturn(dish1);
//    }
}
