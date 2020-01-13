package ru.lena.restaurant.service;

import ru.lena.restaurant.model.User;
import ru.lena.restaurant.to.UserTo;

import java.util.List;

public interface UserService {
    User create(User user);
    void delete(long id);
    User get(long id);
    User getByEmail(String email);
    List<User> getAll();
    void update(UserTo userTo);
    void update(User user);
    void enable(long id, boolean enabled);
}
