package org.farozy.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.PermissionDto;
import org.farozy.entity.Permission;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.permission.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("permissionController")
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Permission>>> retrieveAll() {
        List<Permission> permissions = permissionService.findAll();
        String message = "Permissions retrieved all successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> retrieveById(@PathVariable Long id) {
        Permission permission = permissionService.findById(id);
        String message = "Permission retrieved by id successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, permission);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Permission>> create(@RequestBody @Valid PermissionDto request) {
        Permission newPermission = permissionService.save(request);
        String message = "Permission created successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, newPermission);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> edit(@PathVariable Long id, @RequestBody @Valid PermissionDto request) {
        Permission permission = permissionService.update(id, request);
        String message = "Permission updated successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, permission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> destroy(@PathVariable Long id) {
        permissionService.delete(id);
        String message = "Permission deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

}
