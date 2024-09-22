package org.farozy.service;

import org.farozy.dto.FoodDto;
import org.farozy.entity.Food;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

    List<Food> findAll();

    Food findById(Long id);

    Food save(FoodDto request, MultipartFile imageFile);

    Food update(Long id, FoodDto request, MultipartFile imageFile);

    void delete(Long id);

}
