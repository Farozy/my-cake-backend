package org.farozy.repository;

import org.farozy.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    Favorite findByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserId(Long userId);
}
