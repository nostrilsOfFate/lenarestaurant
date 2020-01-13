package ru.lena.restaurant.service;

import ru.lena.restaurant.model.UserVote;

public interface UserVoteService {
    boolean isVoteExists(long userId);
    UserVote getByUserId(long userId);
    void deleteVoteByUserId(long userId);
    UserVote addVote(long userId, long restaurantId);
}
