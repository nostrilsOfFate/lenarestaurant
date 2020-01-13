package ru.lena.restaurant.service;

import ru.lena.restaurant.model.VoteHistory;

import java.time.LocalDate;
import java.util.List;

public interface VoteHistoryService {
    void delete(long id);
    List<VoteHistory> getAll();
    VoteHistory get(long id);
    List<VoteHistory> findAllByRestaurantId(Long id);
    List<VoteHistory> findAllSorted();
    List<VoteHistory> findAllSortedByScore(Integer score);
    List<VoteHistory> findAllSortedBetween(LocalDate startDate, LocalDate endDate);
    void updateVoteHistory();
}
