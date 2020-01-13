package ru.lena.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lena.restaurant.model.AbstractBaseEntity;
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.repository.RestaurantRepository;
import ru.lena.restaurant.utils.EntityUtil;
import ru.lena.restaurant.utils.exсeption.NotFoundException;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@SpringBootTest
class RestaurantServiceImplTest {

    @Autowired
    private RestaurantService service;

    @Autowired
    private RestaurantRepository repository;

    @Test
    void create() {
        Restaurant expected = new Restaurant(null,"new Restaurant",0,null);
        Restaurant created = service.create(EntityUtil.asTo(expected));
        assertNotNull(created);
        Long newId = created.getId();
        expected.setId(newId);
        assertEquals(expected, created);
    }

    @Test
    void delete() {
        service.delete(1L);
        assertThrows(NotFoundException.class, () -> service.get(1L));
    }
    @Test
    void deletedNotFound() throws Exception {
        service.delete(1L);
        assertThrows(NotFoundException.class, () -> service.delete(1));
    }

    @Test
    void getAll() {
        List<Restaurant> expected = repository.findAll();
        assertNotNull(expected);
        List<Restaurant> actual = service.getAll();
        assertNotNull(actual);
        expected.sort(Comparator.comparing(AbstractBaseEntity::getId));
        actual.sort(Comparator.comparing(AbstractBaseEntity::getId));
        assertEquals(expected, actual);
    }

    @Test
    void get() {
        Restaurant expected = repository.findById(1L).orElse(null);
        assertNotNull(expected);
        Restaurant actual = service.get(1L);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getNotFound() {
    assertThrows(NotFoundException.class, () -> service.get(22));
    }

    @Test
    void update() {
        Restaurant toUpdate = repository.findById(1L).orElse(null);
        assertNotNull(toUpdate);
        toUpdate.setName("New Name");
        service.update(EntityUtil.asTo(toUpdate));
        Restaurant actual = service.get(1L);
        assertNotNull(actual);
        assertEquals(toUpdate, actual);
    }

    @Test
    void testUpdate() {
        Restaurant toUpdate = repository.findById(1L).orElse(null);
        assertNotNull(toUpdate);
        toUpdate.setName("New Name");
        service.update(toUpdate);
        Restaurant actual = service.get(1L);
        assertNotNull(actual);
        assertEquals(toUpdate, actual);
    }

    @Test
    void findAllSortedByName() {
        List<Restaurant> expected = repository.findAll();
        assertNotNull(expected);
        List<Restaurant> actual = service.findAllSortedByName();
        assertNotNull(actual);
        expected.sort(Comparator.comparing(Restaurant::getName));
        actual.sort(Comparator.comparing(Restaurant::getName));
        assertEquals(expected, actual);
    }

    @Test
    void getByName() {
        Restaurant expected = repository.findById(2L).orElse(null);
        assertNotNull(expected);
        Restaurant actual = service.getByName("Новый элитный ресторан 5 звезд");
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}