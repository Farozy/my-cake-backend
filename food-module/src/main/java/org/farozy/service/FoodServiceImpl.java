package org.farozy.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.FoodDto;
import org.farozy.entity.Category;
import org.farozy.entity.Food;
import org.farozy.entity.Store;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.helper.FileUploadHelper;
import org.farozy.repository.CategoryRepository;
import org.farozy.repository.FoodRepository;
import org.farozy.repository.StoreRepository;
import org.farozy.utility.FileUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private static final String foodModule = "food-module";
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<Food> findAll() {
        try {
            return foodRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching foods " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Food findById(Long id) {
        return getFoodById(id);
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Food save(FoodDto request, MultipartFile imageFile) {
        try {
            return saveOrUpdateFood(null, request, imageFile);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving food " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Food update(Long id, FoodDto request, MultipartFile imageFile) {
        try {
            return saveOrUpdateFood(id, request, imageFile);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating food " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public void delete(Long id) {
        try {
            Food food = getFoodById(id);

            FileUtils.deleteFile(foodModule, food.getImage());

            foodRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting by id food " + ex.getMessage());
        }
    }

    private Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The food with the specified ID does not exist"
                ));
    }

    private Food saveOrUpdateFood(Long id, FoodDto request, MultipartFile imageFile) throws IOException {
        Food food = (id == null) ? new Food() : getFoodById(id);

        String newImageFood = null;
        try {
            newImageFood = FileUploadHelper.processSaveImage(foodModule, imageFile);
            food.setImage(newImageFood);

            return saveOrUpdateFoodFromRequest(food, request);
        } catch (Exception e) {
            if (newImageFood != null) FileUtils.deleteFile(foodModule, newImageFood);

            throw new RuntimeException("Failed to save food and image: " + e.getMessage(), e);
        }
    }

    public Food saveOrUpdateFoodFromRequest(Food food, FoodDto request) {
        try {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The category with the specified ID does not exist"
                    ));

            Store store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The store with the specified ID does not exist"
                    ));

            food.setName(request.getName());
            food.setPrice(request.getPrice());
            food.setDescription(request.getDescription());
            food.setStock(request.getStock());
            food.setDiscount(request.getDiscount());

            food.setCategory(category);
            food.setStore(store);

            return foodRepository.save(food);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
