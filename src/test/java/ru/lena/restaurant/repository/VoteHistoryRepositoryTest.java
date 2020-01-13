package ru.lena.restaurant.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.model.VoteHistory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@SpringBootTest
public class VoteHistoryRepositoryTest {

    @Autowired
    private VoteHistoryRepository voteHistoryRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private DishRepository dishRepository;

    @Test
    @Transactional
    public void saveTest() {
        Restaurant restaurant = restaurantRepository.findById(1L).orElse(null);
        assertNotNull(restaurant);
        Dish dish1 = dishRepository.findById(7L).orElse(null);
        assertNotNull(dish1);
        Dish dish2 = dishRepository.findById(8L).orElse(null);
        assertNotNull(dish1);
        List<Dish> dishes = new ArrayList<>(Arrays.asList(dish1, dish2));

        VoteHistory newVoteHistoryEntity = new VoteHistory(null, restaurant, dishes, 35);
        VoteHistory actual = voteHistoryRepository.save(newVoteHistoryEntity);
        assertNotNull(actual);
        assertEquals(35, actual.getScore());
        assertEquals(1, actual.getRestaurant().getId());
        assertEquals(2, actual.getMenuOfDay().size());
    }

    @Test
    void deleteByIdTest() {
        int result = voteHistoryRepository.delete(1L);
        assertTrue(result != 0);
        assertFalse(voteHistoryRepository.existsById(1L));
    }

    @Test
    public void getTest() {
        VoteHistory actual = voteHistoryRepository.findById(1L).orElse(null);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertNotNull(actual.getRestaurant());
        assertNotNull(actual.getMenuOfDay());
        assertTrue(actual.getMenuOfDay().size() > 0);
    }

    @Test
    void getAll() {
        List<VoteHistory> actual = voteHistoryRepository.findAll();
        assertNotNull(actual);
        assertEquals(9, actual.size());
    }

    @Test
    void findAllByOrderByVotTimeDescTest() {//List<VoteHistory> findAllByOrderByVotTimeDesc();


//обьекты из тестовой базы с различным временем
    }
}
