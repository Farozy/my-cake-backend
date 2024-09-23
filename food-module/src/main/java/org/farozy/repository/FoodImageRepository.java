package org.farozy.repository;

import org.farozy.entity.Food;
import org.farozy.entity.FoodImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodImageRepository extends JpaRepository<FoodImage, Long> {
    List<FoodImage> findByFoodId(Food foodId);

    void deleteByFoodId(Food foodId);
}