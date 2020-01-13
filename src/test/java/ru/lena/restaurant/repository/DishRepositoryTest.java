package ru.lena.restaurant.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lena.restaurant.model.Dish;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@SpringBootTest
public class DishRepositoryTest {

    @Autowired
    private DishRepository dishRepository;

    @Test
    public void saveTest() {
        Dish dish = new Dish("chiken", BigDecimal.valueOf(240), true);
        Dish saved = dishRepository.save(dish);
        assertNotNull(saved);
        Dish newDish = dishRepository.findById(saved.getId()).orElse(null);
        assertNotNull(newDish);
        assertEquals(dish.getName(), newDish.getName());
    }

    @Test
    public void update() {
        Dish expected = dishRepository.findById(1L).orElse(null);
        assertNotNull(expected);
        expected.setName("asdcc");
        expected.setPrice(BigDecimal.valueOf(234));
        expected.setTodayMenuDish(false);
        Dish actual = dishRepository.save(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void deleteByIdTest() {
        int b = dishRepository.delete(1L);
        assertTrue(b > 0);
        Dish deleted = dishRepository.findById(1L).orElse(null);
        assertNull(deleted);
    }

    @Test
    public void getTest() {
        Dish dish = dishRepository.findById(1L).orElse(null);
        assertNotNull(dish);
        assertTrue(dish.isTodayMenuDish());
        assertEquals(BigDecimal.valueOf(50).doubleValue(), dish.getPrice().doubleValue());
        assertEquals("компот", dish.getName());
    }

    @Test
    public void getAllTest() {
        List<Dish> actual = dishRepository.findAll();
        assertNotNull(actual);
        assertTrue(actual.size() > 0);
    }

}
