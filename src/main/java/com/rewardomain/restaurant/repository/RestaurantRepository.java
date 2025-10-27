package com.rewardomain.restaurant.repository;
import com.rewardomain.restaurant.bean.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long>
{
    Restaurant findByNumber(long number);
}
