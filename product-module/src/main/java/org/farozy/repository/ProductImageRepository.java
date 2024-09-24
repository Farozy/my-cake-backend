package org.farozy.repository;

import org.farozy.entity.Product;
import org.farozy.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Product productId);

    void deleteByProductId(Product productId);
}