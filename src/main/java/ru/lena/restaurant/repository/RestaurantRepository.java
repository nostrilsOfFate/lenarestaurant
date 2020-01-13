package ru.lena.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.model.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByOrderByNameDesc();

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant u WHERE u.id=:id")
    int delete(@Param("id") long id);

    Restaurant findByName(String name);
}
