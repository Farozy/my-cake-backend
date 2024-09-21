package org.farozy.service;

import org.farozy.dto.StoreDto;
import org.farozy.entity.Store;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreService {

    List<Store> findAll();

    Store findById(Long id);

    Store save(StoreDto request, MultipartFile imageFile);

    Store update(Long id, StoreDto request, MultipartFile imageFile);

    void delete(Long id);

}
