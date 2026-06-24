package com.society.backend.controller.auth;
import com.society.backend.dto.LoginRequest;
import com.society.backend.dto.LoginResponse;
import com.society.backend.dto.MemberLoginRequest;
import com.society.backend.dto.MemberLoginResponse;
import com.society.backend.entity.Society;
import com.society.backend.entity.User;
import com.society.backend.repository.SocietyRepository;
import com.society.backend.repository.UserRepository;
import com.society.backend.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;
    private final SocietyRepository societyRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,SocietyRepository societyRepository,JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.societyRepository = societyRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Long societyId = null;
        String societyName = null;
        String role = null;
        Long memberId = null;
        String upi = null;

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

        if (user.getMember() != null) {
            memberId = user.getMember().getId();
        }
        if (societyId != null){
        Society society = societyRepository.findById(societyId)
            .orElseThrow(() -> new RuntimeException("Society not found"));
            if (Boolean.TRUE.equals(society.getUpi1Active())) {
                upi = society.getUpi1() != null ? society.getUpi1() : null;
            }else {
                        upi = society.getUpi2() != null ? society.getUpi2() : null;
                    }
        }

        LoginResponse response = new LoginResponse(
                token,
                societyId,
                societyName,
                role,
                user.getId(),
                user.getName(),
                memberId,
                upi
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