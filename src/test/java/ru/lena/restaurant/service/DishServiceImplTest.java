package ru.lena.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.repository.DishRepository;
import ru.lena.restaurant.to.DishTo;
import ru.lena.restaurant.utils.EntityUtil;
import ru.lena.restaurant.utils.exсeption.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.lena.restaurant.utils.ValidationUtil.getRootCause;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@SpringBootTest
public class DishServiceImplTest {

    @Autowired
    private DishService service;

    @Autowired
    private DishRepository repository;

    @Test
    void createTest() throws Exception {
        Dish expected = new Dish(null, "new Dish", BigDecimal.valueOf(240), true);
        Dish created = service.create(EntityUtil.asTo(expected));
        assertNotNull(created);
        Long newId = created.getId();
        expected.setId(newId);
        assertEquals(expected, created);
    }

    @Test
    void deleteTest() throws Exception {
        service.delete(1L);
        assertThrows(NotFoundException.class, () -> service.get(1L));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(22));
    }

    @Test
    void getAll() {
        List<Dish> expected = repository.findAll();
        assertNotNull(expected);
        List<Dish> actual = service.getAll();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void get() {
        Dish dish = service.get(1L);
        assertNotNull(dish);
        assertEquals(1L, dish.getId());
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(22));
    }

    @Test
    void updateFromTo() {
        DishTo expected = new DishTo(1L, "Мясо", BigDecimal.valueOf(350L), true);
        service.update(expected);
        Dish actual = service.get(1L);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice().doubleValue(), actual.getPrice().doubleValue());
        assertEquals(expected.isTodayMenuDish(), actual.isTodayMenuDish());
    }

    @Test
    void update() {
        Dish expected = new Dish(1L, "Мясо", BigDecimal.valueOf(350L), true);
        service.update(expected);
        Dish actual = service.get(1L);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice().doubleValue(), actual.getPrice().doubleValue());
        assertEquals(expected.isTodayMenuDish(), actual.isTodayMenuDish());
    }

    @Test
    void setTodayMenu() {
        Dish dish = repository.findById(2L).orElse(null);
        assertNotNull(dish);
        dish.setTodayMenuDish(false);
        repository.save(dish);

        Dish expected = repository.findById(2L).orElse(null);
        assertNotNull(expected);
        assertFalse(expected.isTodayMenuDish());

        expected.setTodayMenuDish(true);
        service.setTodayMenu(expected.getId(), true);
        Dish actual = repository.findById(expected.getId()).orElse(null);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new DishTo(null, "  ", BigDecimal.valueOf(20), false)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new DishTo(null, "aijsdn", null, false)), ConstraintViolationException.class);
    }

    //  Check root cause in JUnit: https://github.com/junit-team/junit4/pull/778
    <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        assertThrows(exceptionClass, () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }
}
