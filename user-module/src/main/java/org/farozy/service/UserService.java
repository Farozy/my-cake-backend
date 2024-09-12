package org.farozy.service;

import org.farozy.dto.UserDto;
import org.farozy.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    User save(UserDto request, MultipartFile imageFile);

    User update(Long id, UserDto request, MultipartFile imageFile);

    void delete(Long id);

}