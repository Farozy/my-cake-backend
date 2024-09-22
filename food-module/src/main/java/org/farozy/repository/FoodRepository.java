package org.farozy.repository;

import org.farozy.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    // Mencari makanan berdasarkan kategori
    List<Food> findByCategoryId(Long categoryId);

    // Mencari makanan berdasarkan nama (case insensitive)
    List<Food> findByNameContainingIgnoreCase(String name);

    // Mencari makanan yang tersedia
    List<Food> findByAvailableTrue();

    // Mencari makanan dengan diskon
    List<Food> findByDiscountGreaterThan(BigDecimal discount);
}
