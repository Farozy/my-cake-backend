package org.farozy.controller;

import lombok.RequiredArgsConstructor;
import org.farozy.dto.UserDto;
import org.farozy.entity.User;
import org.farozy.helper.FillMapHelper;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.UserService;
import org.farozy.validation.CreateGroup;
import org.farozy.validation.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private static final String userModule = "user-module";
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> retrieveAll()
            throws IOException, IllegalAccessException {
        List<User> users = userService.findAll();
        List<Map<String, Object>> userMaps = new ArrayList<>();
        String message = "Users retrieved all successfully";

        for (User user : users) {
            Map<String, Object> userMap = FillMapHelper.fillMapData(user, userModule);
            userMaps.add(userMap);
        }

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, userMaps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> retrieveById(@PathVariable Long id)
            throws IOException, IllegalAccessException {
        User user = userService.findById(id);
        Map<String, Object> userMap = FillMapHelper.fillMapData(user, userModule);
        String message = "User retrieved by id successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, userMap);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> create(
            @Validated(CreateGroup.class)  @ModelAttribute UserDto request,
            @RequestParam("image") MultipartFile file
    ) throws IOException, IllegalAccessException {
        User createdUser = userService.save(request, file);
        String message = "User created successfully";
        Map<String, Object> userMap = FillMapHelper.fillMapData(createdUser, userModule);

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, userMap);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> edit(
            @PathVariable Long id, @Validated(UpdateGroup.class)  @ModelAttribute UserDto request,
            @RequestParam("image") MultipartFile file
    ) throws IOException, IllegalAccessException {
        User updatedUser = userService.update(id, request, file);
        String message = "User updated successfully";
        Map<String, Object> userMap = FillMapHelper.fillMapData(updatedUser, userModule);

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, userMap);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> destroy(@PathVariable Long id) {
        userService.delete(id);
        String message = "User deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

}
