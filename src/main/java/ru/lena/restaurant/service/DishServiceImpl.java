package ru.lena.restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.repository.DishRepository;
import ru.lena.restaurant.to.DishTo;
import ru.lena.restaurant.utils.EntityUtil;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.lena.restaurant.utils.ValidationUtil.checkNotFound;
import static ru.lena.restaurant.utils.ValidationUtil.checkNotFoundWithId;

@Service
public class DishServiceImpl implements DishService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final DishRepository repository;

    @Autowired
    public DishServiceImpl(DishRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Override
    public Dish create(DishTo dishTo) {
        log.info("create dish from dishTo {}", dishTo);
        checkNotFound(dishTo, "dishTo must not be null");
        return repository.save(EntityUtil.createNewFromTo(dishTo));
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Override
    public void delete(long id) {
        log.info("delete dish by id {}", id);
        checkNotFoundWithId((repository.delete(id) != 0), id);
    }

    @Cacheable("dishes")
    @Override
    public List<Dish> getAll() {
        log.info("get all dishes");
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Dish::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Dish get(long id) {
        log.info("get dish by id {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    @Override
    public void update(@NotNull DishTo dishTo) {
        log.info("update dish from dishTo {}", dishTo);
        Dish dish = get(dishTo.id());
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        dish.setTodayMenuDish(dishTo.isTodayMenuDish());
        repository.save(dish);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    @Override
    public void update(Dish dish) {
        log.info("update dish from dish{}", dish);
        checkNotFound(dish, "dishTo must not be null");
        repository.save(dish);
    }

    @Override
    public void setTodayMenu(@NotNull Long dishId, boolean isDishToday) {
        log.info("set today menu for dish with id {} to {}", dishId, isDishToday);
        Dish dish = get(dishId);
        dish.setTodayMenuDish(isDishToday);
        update(dish);
    }

}
