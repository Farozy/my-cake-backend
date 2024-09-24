package org.farozy.service.productImage;

import org.farozy.dto.ProductImageDto;
import org.farozy.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    ProductImage findById(Long id);

    List<ProductImage> findByProductId(Long id);

    void uploadProductImage(ProductImageDto request, List<MultipartFile> images);

    ProductImage updateProductImage(Long id, ProductImageDto request, MultipartFile image);

    void delete(Long id);

    void deleteByProductId(Long id);

}