package com.society.backend.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<User> create(@RequestBody User user) {

        return ResponseEntity.ok(service.save(user));
    }

    // =========================
    // Get All Users
    // =========================

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    // =========================
    // Get User By ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    // =========================
    // Update User
    // =========================

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