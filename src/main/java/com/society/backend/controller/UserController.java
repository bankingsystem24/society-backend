package com.society.backend.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.UserRequest;
import com.society.backend.dto.UserResponse;
import com.society.backend.entity.User;
import com.society.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService service;

    // =========================
    // Create User
    // =========================

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserRequest req) {
        return ResponseEntity.ok(service.createUser(req));
    }

    // =========================
    // Get All Users
    // =========================

    @GetMapping
    public List<UserResponse> getAll(@RequestParam(required = false) Long societyId) {
        return service.getAll(societyId);
    }

    // =========================
    // Get User By ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(
            @RequestParam Long id,
            @RequestParam Boolean active) {

        service.updateStatus(id, active);

        return ResponseEntity.ok("Status updated");
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id,
                                       @RequestBody User user) {

        return ResponseEntity.ok(service.update(id, user));
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