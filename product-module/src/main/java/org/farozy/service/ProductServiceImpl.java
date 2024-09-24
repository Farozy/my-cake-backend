package org.farozy.service;


import lombok.RequiredArgsConstructor;
import org.farozy.dto.ProductDto;
import org.farozy.entity.Category;
import org.farozy.entity.Product;
import org.farozy.entity.Store;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.helper.FileUploadHelper;
import org.farozy.repository.CategoryRepository;
import org.farozy.repository.ProductRepository;
import org.farozy.repository.StoreRepository;
import org.farozy.utility.FileUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    private static final String ProductModule = "product-module";
    private static final String thumbnails = "thumbnails";

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<Product> findAll() {
        try {
            return productRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching all product: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Product findById(Long id) {
        try {
            return getProductById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching by id product: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserCreate
    public Product create(ProductDto request, MultipartFile imageFile) {
        try {
            getProductByname(request.getName());

            return saveOrUpdateProduct(null, request, imageFile);
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving product: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserUpdate
    public Product update(Long id, ProductDto request, MultipartFile imageFile) {
        try {
            return saveOrUpdateProduct(id, request, imageFile);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating product: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserDelete
    public void delete(Long id) {
        try {
            Product product = getProductById(id);

            FileUtils.deleteFile(ProductModule, product.getImageUrl(), thumbnails);

            productRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting by id product: " + ex.getMessage());
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The product with the specified ID does not exist"
                ));
    }

    public void getProductByname(String productName) {
        productRepository.findFirstByName(productName)
                .ifPresent(product -> {
                    throw new ResourceAlreadyExistsException(
                            "Product with name " + productName + " already exists"
                    );
                });
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The category with the specified ID does not exist"
                ));
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The store with the specified ID does not exist"
                ));
    }

    private Product saveOrUpdateProduct(Long id, ProductDto request, MultipartFile imageFile) throws IOException {
        Product product = (id == null) ? new Product() : getProductById(id);
        Category findCategoryById = getCategoryById(request.getCategoryId());
        Store findStoreById = getStoreById(request.getStoreId());

        if (imageFile != null && !imageFile.isEmpty()) {
            if (id != null) FileUtils.deleteFile(ProductModule, product.getImageUrl(), thumbnails);

            product.setImageUrl(FileUploadHelper.processSaveImage(ProductModule, imageFile, thumbnails));
        }

        return saveOrUpdateProductFromRequest(product, request, findCategoryById, findStoreById);
    }

    public Product saveOrUpdateProductFromRequest(Product product, ProductDto request, Category category, Store store) {
        String price = request.getPrice().replaceAll("\\.", "");
        String discount = request.getDiscount() != null ? request.getDiscount().toString() : "0";
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(new BigInteger(price));
        product.setCategory(category);
        product.setStore(store);
        product.setStock(BigInteger.valueOf(request.getStock()));
        product.setDiscount(new BigDecimal(discount));

        return productRepository.save(product);
    }

}

