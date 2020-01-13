package ru.lena.restaurant.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lena.restaurant.model.Role;
import ru.lena.restaurant.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveTest() {
        User user = new User(null, "Иван Васильевич", "ivan@mail.ru", "ivanivan", Role.ROLE_USER, Role.ROLE_USER);
        User saved = userRepository.save(user);
        assertNotNull(saved);
        User userNew = userRepository.findByEmail(user.getEmail());
        assertEquals(user.getEmail(), userNew.getEmail());
    }

    @Test
    public void deleteTest() {
        int i = userRepository.delete(1L);
        assertTrue(i != 0);
        assertFalse(userRepository.existsById(1L));
    }


    @Test
    public void getTest() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);
        assertEquals("user@yandex.ru", user.getEmail());
    }

    @Test
    public void getByEmailTest() {
        User firstUser = userRepository.findByEmail("user@yandex.ru");
        assertNotNull(firstUser);
        assertEquals(1L, firstUser.getId());
    }

    @Test
    public void getAllTest() {
        List<User> actual = userRepository.findAll();
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }
}