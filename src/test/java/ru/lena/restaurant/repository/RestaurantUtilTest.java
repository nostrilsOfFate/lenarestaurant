package ru.lena.restaurant.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lena.restaurant.model.Restaurant;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@SpringBootTest
public class RestaurantUtilTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    public void saveTest() {
        Restaurant expected = new Restaurant(null, "Ресторан се ля ви");
        Restaurant actual = restaurantRepository.save(expected);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getScore(), actual.getScore());
    }

    @Test
    public void deleteByIdTest() {
        restaurantRepository.deleteById(1L);
        Restaurant actual = restaurantRepository.findById(1L).orElse(null);
        assertNull(actual);
    }

    @Test
    public void getTest() {
        Restaurant actual = restaurantRepository.findById(1L).orElse(null);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    public void getAllTest() {
        List<Restaurant> actual = restaurantRepository.findAll();
        assertNotNull(actual);
        assertTrue(actual.size() > 0);
    }

    @Test
    public void findAllByOrderByNameDescTest() {
        List<Restaurant> expected = restaurantRepository.findAll().stream()
                .sorted(Comparator.comparing(Restaurant::getName).reversed())
                .collect(Collectors.toList());
        assertNotNull(expected);
        List<Restaurant> actual = restaurantRepository.findAllByOrderByNameDesc();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
