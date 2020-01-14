package ru.lena.restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.model.VoteHistory;
import ru.lena.restaurant.repository.VoteHistoryRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.lena.restaurant.utils.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteHistoryServiceImpl implements VoteHistoryService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteHistoryRepository repository;
    private final RestaurantService restaurantService;

    @Autowired
    public VoteHistoryServiceImpl(VoteHistoryRepository repository, RestaurantService service) {
        this.repository = repository;
        this.restaurantService = service;
    }

    @CacheEvict(value = "voteHistories", allEntries = true)
    @Override
    public void delete(long id) {
        log.info("delete vote history by id {}", id);
        checkNotFoundWithId((repository.delete(id) != 0), id);
    }

    @Cacheable("voteHistories")
    @Override
    public List<VoteHistory> getAll() {
        log.info("get all vote histories");
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(VoteHistory::getVotTime))
                .collect(Collectors.toList());
    }

    public VoteHistory get(long id) {
        log.info("get vote history by id  {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @Override
    public List<VoteHistory> findAllByRestaurantId(Long id) {
        log.info("find all vote histories by rest id {}", id);
        return findAllSorted().stream()
                .filter(voteHistory -> voteHistory.getRestaurant().getId().equals(id))
                .collect(Collectors.toList());
    }

    @Cacheable("sortedByDateVoteHistories")
    @Override
    public List<VoteHistory> findAllSorted() {
        log.info("find all sorted vote histories");
        return repository.findAllByOrderByVotTimeDesc();
    }

    @Override
    public List<VoteHistory> findAllSortedByScore(Integer score) {
        log.info("find all sorted vote histories by score {}", score);
        return findAllSorted().stream()
                .filter(voteHistory -> voteHistory.getScore().equals(score))
                .sorted(Comparator.comparing(VoteHistory::getVotTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<VoteHistory> findAllSortedBetween(LocalDate startDate, LocalDate endDate) {
        log.info("find all sorted vote histories after date {} and before date {}", startDate, endDate);
        return findAllSorted().stream()
                .filter(voteHistory -> {
                    LocalDate date = voteHistory.getVotTime();
                    return ((date.isAfter(startDate) || date.isEqual(startDate)) &&
                            (date.isBefore(endDate) || date.isEqual(endDate)));
                }).collect(Collectors.toList());
    }


    /**
     * Log all restaurants to history table
     * at 23:00 every day
     */
    @Scheduled(cron = "0 0 23 * * *")
    @CacheEvict(value = "voteHistories", allEntries = true)
    @Transactional
    public void updateVoteHistory() {
        log.info("update all vote histories in day");
        List<Restaurant> restaurants = restaurantService.getAll();
        List<VoteHistory> historyList = restaurants.stream()
                .map(restaurant -> {
                    List<Dish> todayDishes = restaurant.getMenu()
                            .stream().filter(Dish::isTodayMenuDish)
                            .collect(Collectors.toList());
                    return new VoteHistory(restaurant, todayDishes, restaurant.getScore());
                }).collect(Collectors.toList());
        repository.saveAll(historyList);
    }

}
