package ru.lena.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.lena.restaurant.model.VoteHistory;

import java.util.List;

@Repository
public interface VoteHistoryRepository extends JpaRepository<VoteHistory, Long> {

    List<VoteHistory> findAllByOrderByVotTimeDesc();

    @Transactional
    @Modifying
    @Query("DELETE FROM VoteHistory u WHERE u.id=:id")
    int delete(@Param("id") long id);
}
