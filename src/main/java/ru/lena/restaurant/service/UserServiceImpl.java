package ru.lena.restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.lena.restaurant.model.AuthorizedUser;
import ru.lena.restaurant.model.User;
import ru.lena.restaurant.repository.UserRepository;
import ru.lena.restaurant.to.UserTo;
import ru.lena.restaurant.utils.EntityUtil;

import java.util.List;

import static ru.lena.restaurant.utils.EntityUtil.prepareToSave;
import static ru.lena.restaurant.utils.ValidationUtil.checkNotFound;
import static ru.lena.restaurant.utils.ValidationUtil.checkNotFoundWithId;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserServiceImpl implements UserDetailsService, UserService {
    private final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        checkNotFound(user, "user must not be null");
        return prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void delete(long id) {
        log.info("delete user by id {}", id);
        checkNotFoundWithId((repository.delete(id) != 0), id);
    }

    @Override
    public User get(long id) {
        log.info("get user by id {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @Override
    public User getByEmail(String email) {
        log.info("get user by email {}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.findByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    @Override
    public List<User> getAll() {
        log.info("get all users");
        return repository.findAll(SORT_NAME_EMAIL);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    public void update(UserTo userTo) {
        log.info("update user by userTo {}", userTo);
        User user = get(userTo.id());
        prepareAndSave(EntityUtil.updateFromTo(user, userTo));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void update(User user) {
        log.info("update user by user {}", user);
        Assert.notNull(user, "user must not be null");
        prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    public void enable(long id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = get(id);
        user.setEnabled(enabled);
        repository.save(user);  // !! need only for JDBC implementation
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("load user by user email {}", email);
        User user = repository.findByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user) {
        log.info("prepare and save {}", user);
        return repository.save(prepareToSave(user, passwordEncoder));
    }
}
