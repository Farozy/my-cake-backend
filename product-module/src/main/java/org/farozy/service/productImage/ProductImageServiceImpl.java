package org.farozy.service.productImage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.ProductImageDto;
import org.farozy.entity.Product;
import org.farozy.entity.ProductImage;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.helper.FileUploadHelper;
import org.farozy.helper.FileUploadProperties;
import org.farozy.repository.ProductImageRepository;
import org.farozy.repository.ProductRepository;
import org.farozy.utility.FileUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private static final String productModule = "product-module";
    private static final String detailsPath = "details";
    private final FileUploadProperties fileUploadProperties;

    @Override
    @Transactional
    @UserPermission.UserRead
    public ProductImage findById(Long id) {
        try {
            return getProductImageById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<ProductImage> findByProductId(Long id) {
        try {
            Product getProductById = getProductById(id);

            return productImageRepository.findByProductId(getProductById);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public void uploadProductImage(ProductImageDto request, List<MultipartFile> images) {
        try {
            Product getProductById = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The product with the specified ID does not exist"
                    ));

            List<ProductImage> listProductImages = productImageRepository.findByProductId(getProductById);

            int totalImagesAfterUpload = listProductImages.size() + images.size();

            if (totalImagesAfterUpload > fileUploadProperties.getMaxProductImage()) {
                throw new RuntimeException("Cannot upload product image; maximum of 5 images reached");
            }

            for (MultipartFile image : images) {
                ProductImage productImage = new ProductImage();

                String imageUrl = FileUploadHelper.processSaveImage(productModule, image, detailsPath);

                productImage.setProductId(getProductById);
                productImage.setImageUrl(imageUrl);
                productImageRepository.save(productImage);
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to upload product image: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public ProductImage updateProductImage(Long id, ProductImageDto request, MultipartFile image) {
        try {
            ProductImage productImage = getProductImageById(id);

            FileUtils.deleteFile(productModule, productImage.getImageUrl(), detailsPath);

            String imageUrl = FileUploadHelper.processSaveImage(productModule, image, detailsPath);

            productImage.setImageUrl(imageUrl);
            return productImageRepository.save(productImage);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public void delete(Long id) {
        try {
            ProductImage findProductImageById = getProductImageById(id);

            FileUtils.deleteFile(productModule, findProductImageById.getImageUrl(), detailsPath);

            productImageRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting product image by id " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public void deleteByProductId(Long id) {
        try {
            productRepository.findById(id);

            Product getProductById = getProductById(id);

            List<ProductImage> listProductImages = productImageRepository.findByProductId(getProductById);

            if (!listProductImages.isEmpty()) {
                for (ProductImage productImage : listProductImages) {
                    String fileName = productImage.getImageUrl();
                    FileUtils.deleteFile(productModule, fileName, detailsPath);
                }
            }

            productImageRepository.deleteByProductId(getProductById);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting product image by product ID " + ex.getMessage());
        }
    }

    private ProductImage getProductImageById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The product image with the specified ID does not exist"
                ));
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The product with the specified ID does not exist"
                ));
    }
}
