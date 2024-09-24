package org.farozy.service.product;

import org.farozy.dto.ProductDto;
import org.farozy.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);

    Product create(ProductDto request, MultipartFile imageFile);

    Product update(Long id, ProductDto request, MultipartFile imageFile);

    void delete(Long id);

}