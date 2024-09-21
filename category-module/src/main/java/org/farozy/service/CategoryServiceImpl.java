package org.farozy.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.CategoryDto;
import org.farozy.entity.Category;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.helper.FileUploadHelper;
import org.farozy.repository.CategoryRepository;
import org.farozy.utility.FileUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final String categoryModule = "category-module";

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<Category> findAll() {
        try {
            return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching categories: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Category findById(Long id) {
        return getCategoryById(id);
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Category save(CategoryDto request, MultipartFile imageFile) {
        try {
            return saveOrUpdateCategory(null, request, imageFile);
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving category: " + ex.getMessage());
        }
    }

    @Override
    public Category update(Long id, CategoryDto request, MultipartFile imageFile) {
        try {
            return saveOrUpdateCategory(id, request, imageFile);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating category: " + ex.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Category category = getCategoryById(id);

            FileUtils.deleteFile(categoryModule, category.getImage());

            categoryRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting by id category: " + ex.getMessage());
        }
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The category with the specified ID does not exist"
                ));
    }

    private Category saveOrUpdateCategory(Long id, CategoryDto request, MultipartFile imageFile) throws IOException {
        Category category = (id == null) ? new Category() : getCategoryById(id);

        checkCategoryName(id, request.getName());

        if (id != null) FileUtils.deleteFile(categoryModule, category.getImage());

        category.setImage(FileUploadHelper.processSaveImage(categoryModule, imageFile));

        return saveOrUpdateCategoryFromRequest(category, request);
    }

    private void checkCategoryName(Long id, String categoryName) {
        categoryRepository.findByName(categoryName)
                .filter(existingCategory -> !existingCategory.getId().equals(id))
                .ifPresent(cate -> {
                    throw new ResourceAlreadyExistsException(
                            "Category with name " + cate.getName() + " already exists");
                });
    }

    public Category saveOrUpdateCategoryFromRequest(Category category, CategoryDto request) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryRepository.save(category);
    }
}