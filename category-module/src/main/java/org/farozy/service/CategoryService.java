package org.farozy.service;

import org.farozy.dto.CategoryDto;
import org.farozy.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    Category save(CategoryDto request, MultipartFile imageFile);

    Category update(Long id, CategoryDto request, MultipartFile imageFile);

    void delete(Long id);

}