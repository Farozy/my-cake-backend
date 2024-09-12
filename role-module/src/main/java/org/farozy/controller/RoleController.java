package org.farozy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.RoleDto;
import org.farozy.entity.Role;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.role.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("roleController")
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Role>>> retrieveAll() {
        List<Role> roles = roleService.findAll();
        String message = "Roles retrieved all successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> retrieveById(@PathVariable Long id) {
        Role foundRoleById = roleService.findById(id);
        String message = "Role retrieved by id successfully";
        return ResponseHelper.buildResponseData(HttpStatus.OK, message, foundRoleById);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Role>> create(@RequestBody @Valid RoleDto request) {
        Role createdRole = roleService.save(request);
        String message = "Role created successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, createdRole);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> edit(@PathVariable Long id, @RequestBody @Valid RoleDto request) {
        Role updatedRole = roleService.update(id, request);
        String message = "Role updated successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> destroy(@PathVariable Long id) {
        roleService.delete(id);
        String message = "Role deleted successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

}