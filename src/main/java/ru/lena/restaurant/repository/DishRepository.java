package ru.lena.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.lena.restaurant.model.Dish;

import java.util.List;

@Repository
public interface DishRepository  extends JpaRepository<Dish, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish u WHERE u.id=:id")
    int delete(@Param("id") long id);

}
