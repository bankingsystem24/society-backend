package com.society.backend.controller.auth;
import com.society.backend.dto.LoginRequest;
import com.society.backend.dto.LoginResponse;
import com.society.backend.dto.MemberLoginRequest;
import com.society.backend.dto.MemberLoginResponse;
import com.society.backend.entity.User;
import com.society.backend.repository.UserRepository;
import com.society.backend.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Long societyId = null;
        String societyName = null;
        String role = null;

        User user = userRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(request.getUsername())
                        && u.getPassword().equals(request.getPassword()))
                .findFirst()
                .orElse(null);


        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        } 

        String token = jwtUtil.generateToken(user.getUsername());

        role = user.getRole().name();

        if (user.getSociety() != null) {
            societyId = user.getSociety().getId();
            societyName = user.getSociety().getSocietyName();
        }

        LoginResponse response = new LoginResponse(
                token,
                societyId,
                societyName,
                role,
                user.getId(),
                user.getName(),
                user.getMember().getId()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/memberlogin")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequest request) {
        User user = userRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(request.getUsername())
                        && u.getPassword().equals(request.getPassword()))
                .findFirst()
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getUsername());
            MemberLoginResponse response = new MemberLoginResponse(
            token,
            user.getSociety().getId(),
            user.getSociety().getSocietyName(),
            user.getMember().getId(),
            user.getMember().getName(),
            user.getRole().name(),
            user.getActive()
    );
    if (!user.getActive()) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Your account is inactive. Please contact the administrator.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(error);   
    }
        return ResponseEntity.ok(response);
    }

}