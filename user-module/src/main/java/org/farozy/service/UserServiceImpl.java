package org.farozy.service;

import lombok.RequiredArgsConstructor;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.repository.UserRepository;
import org.farozy.dto.UserDto;
import org.farozy.entity.Role;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.helper.FileUploadHelper;
import org.farozy.repository.RoleRepository;
import org.farozy.utility.EntityUtils;
import org.farozy.utility.FileUtils;
import org.farozy.utility.UserUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String userModule = "user-module";
    private static final String DEFAULT_ROLE_NAME = "USER";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserUtils userUtils;
    private static final String addPath = "thumbnails";

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<User> findAll() {
        try {
            return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching all user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public User findById(Long id) {
        try {
            return userUtils.getUserById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching by id user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserCreate
    public User save(UserDto request, MultipartFile imageFile) {
        try {
            return saveOrUpdateUser(null, request, imageFile);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserUpdate
    public User update(Long id, UserDto request, MultipartFile imageFile) {
        try {
            User user = userUtils.getUserById(id);

            return saveOrUpdateUser(user.getId(), request, imageFile);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserDelete
    public void delete(Long id) {
        try {
            User user = userUtils.getUserById(id);

            String userImage = user.getImage();
            if (userImage != null && !userImage.isEmpty()) FileUtils.deleteFile(userModule, userImage, addPath);

            userRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting user: " + ex.getMessage());
        }
    }

    private User saveOrUpdateUser(Long id, UserDto request, MultipartFile imageFile) throws IOException {

        User user;
        String passwordUser;
        if (id == null) {
            checkingUserByUsernameOrEmail(request);

            user = new User();
            passwordUser = passwordEncoder.encode(request.getPassword());
        } else {
            user = userUtils.getUserById(id);

            userUtils.validateEmailUniqueness(request.getEmail());

            passwordUser = user.getPassword();
        }

        BeanUtils.copyProperties(request, user, id != null ? "password" : "");

        user.setPassword(passwordUser);

        handleUserImage(id, user, imageFile);

        handleUserRoles(user, request.getRole());

        return userRepository.save(user);
    }

    private void checkingUserByUsernameOrEmail(UserDto request) {
        userUtils.validateUsernameUniqueness(request.getUsername());
        userUtils.validateEmailUniqueness(request.getEmail());
    }

    private void handleUserImage(Long id, User user, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            if (id != null && user.getImage() != null) {
                FileUtils.deleteFile(userModule, user.getImage(), addPath);
            }
            String imagePath = FileUploadHelper.processSaveImage(userModule, imageFile, "thumbnails");
            user.setImage(imagePath);
        }
    }

    private void handleUserRoles(User user, String role) {
        Set<Role> roles = new HashSet<>();
        Role assignedRole = (role == null) ? getDefaultRole() : findRole(role);

        roles.add(assignedRole != null ? assignedRole : getDefaultRole());
        user.setRoles(roles);
    }

    private Role findRole(String roleIdentifier) {
        return EntityUtils.findEntity(
                roleIdentifier,
                id -> roleRepository.findById(Long.valueOf(roleIdentifier)),
                name -> roleRepository.findByName(roleIdentifier),
                "role"
        );
    }

    private Role getDefaultRole() {
        return roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new IllegalStateException("Default role not found: " + DEFAULT_ROLE_NAME));
    }

}
