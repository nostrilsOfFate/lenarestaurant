package ru.lena.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.lena.restaurant.model.AbstractBaseEntity;
import ru.lena.restaurant.model.Role;
import ru.lena.restaurant.model.User;
import ru.lena.restaurant.repository.UserRepository;
import ru.lena.restaurant.utils.exсeption.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.lena.restaurant.utils.ValidationUtil.getRootCause;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@Transactional
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    protected UserServiceImpl service;

    @Autowired
    private UserRepository repository;

    @Test
    void create() throws Exception {
        User expected = new User(null, "Иван Васильевич", "ivan@mail.ru", "ivanivan", Role.ROLE_USER, Role.ROLE_USER);
        User created = service.create(expected);
        assertNotNull(created);
        Long newId = created.getId();
        expected.setId(newId);
        assertEquals(expected, created);
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER)));
    }

    @Test
    void delete() throws Exception {
        service.delete(1L);
        assertThrows(NotFoundException.class, () ->
                service.get(1L));
    }

    @Test
    void deletedNotFound() throws Exception {
        service.delete(1L);
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() throws Exception {
        User expected = repository.findById(1L).orElse(null);
        assertNotNull(expected);
        User actual = service.get(1L);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(22));
    }

    @Test
    void getByEmail() throws Exception {
        User expected = repository.findById(2L).orElse(null);
        assertNotNull(expected);
        User actual = service.getByEmail("admin@gmail.com");
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void update() throws Exception {
        User toUpdate = repository.findById(1L).orElse(null);
        assertNotNull(toUpdate);
        toUpdate.setName("New Name");
        service.update(toUpdate);
        User actual = service.get(1L);
        assertNotNull(actual);
        assertEquals(toUpdate, actual);
    }

    @Test
    void getAll() throws Exception {
        List<User> expected = repository.findAll();
        assertNotNull(expected);
        List<User> actual = service.getAll();
        assertNotNull(actual);
        expected.sort(Comparator.comparing(AbstractBaseEntity::getId));
        actual.sort(Comparator.comparing(AbstractBaseEntity::getId));
        assertEquals(expected, actual);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.ROLE_USER)), ConstraintViolationException.class);
    }

    @Test
    void enable() {
        service.enable(1L, false);
        assertFalse(service.get(1L).isEnabled());
        service.enable(1L, true);
        assertTrue(service.get(1L).isEnabled());
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
