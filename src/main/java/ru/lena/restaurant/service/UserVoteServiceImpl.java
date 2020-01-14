package ru.lena.restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.lena.restaurant.model.UserVote;
import ru.lena.restaurant.repository.UserVoteRepository;

import static ru.lena.restaurant.utils.ValidationUtil.checkNotFoundWithId;

@Service
public class UserVoteServiceImpl implements UserVoteService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final UserVoteRepository repository;

    @Autowired
    public UserVoteServiceImpl(UserVoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isVoteExists(long userId) {
        return repository.existsByUserId(userId);
    }

    @Override
    public UserVote getByUserId(long userId) {
        log.debug("get vote for user with id {}", userId);
        return repository.findByUserId(userId).orElse(null);
    }

    @Override
    public void deleteVoteByUserId(long userId) {
        log.debug("Delete vote for user with id {}", userId);
        checkNotFoundWithId((repository.delete(userId) != 0), userId);
    }

    @Override
    public UserVote addVote(long userId, long restaurantId) {
        log.debug("Vote for restaurant {} by user with id {}", restaurantId, userId);
        return repository.save(new UserVote(userId, restaurantId));
    }

    @Override
    public void deleteAllForUSer(long userId) {
        log.debug("Delete all for votes for user with id {}", userId);
        repository.deleteAllByUserId(userId);
    }

    @Scheduled(cron = "0 0 23 * * ?")
    void cleanVotes() {
        repository.deleteAll();
    }
}
