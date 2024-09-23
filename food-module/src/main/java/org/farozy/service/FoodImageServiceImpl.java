package org.farozy.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.FoodImagesUploadDto;
import org.farozy.entity.Food;
import org.farozy.entity.FoodImage;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.helper.FileUploadHelper;
import org.farozy.repository.FoodImageRepository;
import org.farozy.repository.FoodRepository;
import org.farozy.utility.FileUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodImageServiceImpl implements FoodImageService {

    private final FoodImageRepository foodImageRepository;
    private final FoodRepository foodRepository;
    private static final String foodModule = "food-module";
    private static final String detailsPath = "details";

    @Override
    @Transactional
    @UserPermission.UserRead
    public FoodImage findById(Long id) {
        try {
            return getFoodImageById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<FoodImage> findByFoodId(Long id) {
        try {
            Food getFoodById = getFoodById(id);

            List<FoodImage> foodImages = foodImageRepository.findByFoodId(getFoodById);

            if (foodImages.isEmpty()) {
                throw new ResourceNotFoundException("The food image with the specified ID does not exist");
            }

            return foodImages;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public void uploadImages(FoodImagesUploadDto request, List<MultipartFile> images) {
        try {
            Food getFoodById = foodRepository.findById(request.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "The food with the specified ID does not exist"
                    ));

            List<FoodImage> listFoodImages = foodImageRepository.findByFoodId(getFoodById);

            int totalImagesAfterUpload = listFoodImages.size() + images.size();

            if (totalImagesAfterUpload > 5) {
                throw new RuntimeException("Cannot upload images; maximum of 5 images reached");
            }

            for (MultipartFile image : images) {
                FoodImage foodImage = new FoodImage();

                String newFoodImage = FileUploadHelper.processSaveImage(foodModule, image, detailsPath);

                foodImage.setFoodId(getFoodById);
                foodImage.setImageUrl(newFoodImage);
                foodImageRepository.save(foodImage);
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to upload images: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public FoodImage updateUploadImage(Long id, FoodImagesUploadDto request, MultipartFile image) {
        try {
            FoodImage foodImage = getFoodImageById(id);

            FileUtils.deleteFile(foodModule, foodImage.getImageUrl(), detailsPath);

            String newFoodImage = FileUploadHelper.processSaveImage(foodModule, image, detailsPath);

            foodImage.setImageUrl(newFoodImage);
            return foodImageRepository.save(foodImage);
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
            FoodImage findFoodImageById = getFoodImageById(id);

            FileUtils.deleteFile(foodModule, findFoodImageById.getImageUrl(), detailsPath);

            foodImageRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting by id " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public void deleteByFoodId(Long id) {
        try {
            foodRepository.findById(id);

            Food getFoodById = getFoodById(id);

            List<FoodImage> listFoodImages = foodImageRepository.findByFoodId(getFoodById);

            if (listFoodImages.isEmpty()) {
                throw new ResourceNotFoundException("Food images by food ID not found");
            }

            for (FoodImage foodImg : listFoodImages) {
                String fileName = foodImg.getImageUrl();
                FileUtils.deleteFile(foodModule, fileName, detailsPath);
            }

            foodImageRepository.deleteByFoodId(getFoodById);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting by id food " + ex.getMessage());
        }
    }

    private FoodImage getFoodImageById(Long id) {
        return foodImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The food image with the specified ID does not exist"
                ));
    }

    private Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The food with the specified ID does not exist"
                ));
    }

}
