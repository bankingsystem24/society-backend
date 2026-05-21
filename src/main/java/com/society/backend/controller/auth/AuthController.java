package com.society.backend.controller.auth;
import com.society.backend.dto.LoginRequest;
import com.society.backend.dto.LoginResponse;
import com.society.backend.entity.User;
import com.society.backend.repository.UserRepository;
import com.society.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
            LoginResponse response = new LoginResponse(
            token,
            user.getSociety().getId(),
            user.getSociety().getSocietyName() 
    );
        return ResponseEntity.ok(response);
    }


}