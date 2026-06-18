package com.society.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.UserRequest;
import com.society.backend.dto.UserResponse;
import com.society.backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService service;

    // =========================
    // Create User
    // =========================
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
        UserResponse createdUser = service.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // =========================
    // Get All Users (optionally by society)
    // =========================
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll(@RequestParam(required = false) Long societyId) {
        List<UserResponse> users = service.getAll(societyId);
        
        return ResponseEntity.ok(users);
    }

    // =========================
    // Get User By ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        UserResponse user = service.getById(id);
        return ResponseEntity.ok(user);
    }

    // =========================
    // Update User Status
    // =========================
    @PutMapping("/update-status")
    public ResponseEntity<String> updateStatus(
            @RequestParam Long id,
            @RequestParam Boolean active) {

        service.updateStatus(id, active);
        return ResponseEntity.ok("User status updated successfully");
    }

    // =========================
    // Update User
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UserRequest req) {

        UserResponse updatedUser = service.updateFromRequest(id, req);
        return ResponseEntity.ok(updatedUser);
    }

    // =========================
    // Delete User
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}