package org.farozy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.RolePermissionDto;
import org.farozy.entity.RolePermission;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.payload.PermissionRoleResponse;
import org.farozy.payload.RolePermissionResponse;
import org.farozy.service.rolePermission.RolePermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("PermissionManagementController")
@RequestMapping("/api/role-permission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RolePermission>>> listAllRolePermission() {
        List<RolePermission> rolePermissions = rolePermissionService.findAll();
        String message = "Successfully retrieved the list of all role permission";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, rolePermissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolePermission>> getUserRoleById(@PathVariable Long id) {
        RolePermission findUserRole = rolePermissionService.findById(id);
        String message = "Successfully retrieved role permission by ID";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, findUserRole);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<ApiResponse<RolePermissionResponse>> getPermissionByRoleId(@PathVariable Long id) {
        RolePermissionResponse permissionByRoleId = rolePermissionService.findPermissionByRoleId(id);
        String message = "Successfully retrieved permission by role ID";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, permissionByRoleId);
    }

    @GetMapping("/permission/{id}")
    public ResponseEntity<ApiResponse<PermissionRoleResponse>> getRoleByPermissionId(@PathVariable Long id) {
        PermissionRoleResponse roleByPermissionId = rolePermissionService.findRoleByPermissionId(id);
        String message = "Successfully retrieved role by permission ID";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, roleByPermissionId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RolePermission>> createRolePermission(
            @RequestBody @Valid RolePermissionDto request
    ) {
        RolePermission createdRolePermission = rolePermissionService.save(request);
        String message = "Successfully created role permission";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, createdRolePermission);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<RolePermission>> editRolePermission(
            @PathVariable Long id, @RequestBody @Valid RolePermissionDto request
    ) {
        RolePermission updatedRolePermission = rolePermissionService.update(id, request);
        String message = "Successfully updated role permission";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, updatedRolePermission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<RolePermission>> deleteRolePermission(@PathVariable Long id) {
        rolePermissionService.delete(id);
        String message = "Successfully deleted role permission";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

}

