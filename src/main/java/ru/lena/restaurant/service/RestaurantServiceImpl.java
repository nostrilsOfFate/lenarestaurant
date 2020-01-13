package ru.lena.restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.model.UserVote;
import ru.lena.restaurant.model.VoteHistory;
import ru.lena.restaurant.repository.RestaurantRepository;
import ru.lena.restaurant.to.RestaurantTo;
import ru.lena.restaurant.utils.EntityUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.lena.restaurant.utils.ValidationUtil.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantRepository repository;
    private final UserVoteService voteService;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository repository, UserVoteService voteService) {
        this.repository = repository;
        this.voteService = voteService;
    }

    @CacheEvict(value = {"restaurants", "sortedByNameRestaurants"}, allEntries = true)
    @Override
    public Restaurant create(RestaurantTo restaurantTo) {
        log.info("create restaurant from restTo {}", restaurantTo);
        checkNotFound(restaurantTo, "restaurant must not be null");
        return repository.save(EntityUtil.createNewFromTo(restaurantTo));
    }

    @CacheEvict(value = {"restaurants", "sortedByNameRestaurants"}, allEntries = true)
    @Override
    public void delete(long id) {
        log.info("delete restaurant by id {}", id);
        checkNotFoundWithId((repository.delete(id) != 0), id);
    }

    @Cacheable("restaurants")
    @Override
    public List<Restaurant> getAll() {
        log.info("get all restaurants ");
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Restaurant::getScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Restaurant get(long id) {
        log.info("get restaurant by id {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @CacheEvict(value = {"restaurants", "sortedByNameRestaurants"}, allEntries = true)
    @Transactional
    @Override
    public void update(RestaurantTo restaurantTo) {
        log.info("update restaurant by to {}", restaurantTo);
        Restaurant restaurant = get(restaurantTo.id());
        restaurant.setName(restaurantTo.getName());
        checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    @CacheEvict(value = {"restaurants", "sortedByNameRestaurants"}, allEntries = true)
    @Override
    public void update(Restaurant restaurant) {
        log.info("update restaurant by rest {}", restaurant);
        checkNotFound(restaurant, "restaurant must not be null");
        repository.save(restaurant);
    }

    @Cacheable("sortedByNameRestaurants")
    @Override
    public List<Restaurant> findAllSortedByName() {
        log.info("find sorted all restaurants by name");
        return repository.findAllByOrderByNameDesc();
    }

    public Restaurant getByName(String name) {
        log.debug("get restaurant by name {}", name);
        checkNotFound(name, "name must not be null");
        return checkNotFound(repository.findByName(name), "name=" + name);
    }

    @CacheEvict(value = {"restaurants", "sortedByNameRestaurants"}, allEntries = true)
    @Override
    @Transactional
    public void voteForRestaurant(long userId, long restaurantId) {
        checkVotingAllow();

        if (voteService.isVoteExists(userId)) {
            UserVote vote = voteService.getByUserId(userId);
            voteService.deleteVoteByUserId(userId);
            Restaurant old = get(vote.getRestaurantId());
            old.setScore(old.getScore() - 1);
            update(old);
        }
        voteService.addVote(userId, restaurantId);
        Restaurant newOne = get(restaurantId);
        newOne.setScore(newOne.getScore() + 1);
        update(newOne);
    }

    /**
     * Clean all restaurants scores
     * at 23:00 every day
     */
    @Scheduled(cron = "0 10 23 * * ?")
    @CacheEvict(value = {"restaurants", "sortedByNameRestaurants"}, allEntries = true)
    @Transactional
    public void updateVoteHistory() {
        log.info("clean all restaurant scores");
        List<Restaurant> restaurants = getAll();
        restaurants.forEach(restaurant -> restaurant.setScore(0));
        repository.saveAll(restaurants);
    }
}
