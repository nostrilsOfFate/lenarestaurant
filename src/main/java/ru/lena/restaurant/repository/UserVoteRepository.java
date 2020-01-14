package ru.lena.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.lena.restaurant.model.UserVote;

import java.util.Optional;

@Repository
public interface UserVoteRepository extends JpaRepository<UserVote, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM UserVote u WHERE u.id=:id")
    int delete(@Param("id") long id);

    boolean existsByUserId(Long id);

    int deleteAllByUserId(long userId);

    Optional<UserVote> findByUserId(long userId);
}
