package com.society.backend.service;

import org.springframework.stereotype.Service;

import com.society.backend.dto.MemberLoginRequest;
import com.society.backend.dto.MemberLoginResponse;
import com.society.backend.entity.User;
import com.society.backend.repository.UserRepository;
import com.society.backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public MemberLoginResponse login(
            MemberLoginRequest request
    ) {

        User user = userRepository.findAll()
                .stream()
                .filter(u ->

                        u.getUsername().equals(
                                request.getUsername()
                        )

                        &&

                        u.getPassword().equals(
                                request.getPassword()
                        )
                )
                .findFirst()
                .orElse(null);

        if (user == null) {
            return null;
        }

        String token =
                jwtUtil.generateToken(
                        user.getUsername()
                );

        MemberLoginResponse response =
                new MemberLoginResponse(

                        token,

                        user.getSociety().getId(),

                        user.getSociety().getSocietyName(),

                        user.getMember().getId(),

                        user.getMember().getName(),
                        user.getRole().name(),
                        user.getActive()
                );
                
                if(!response.getActive()) {
                        throw new RuntimeException("Member account is inactive");
                }

        return response;
    }
}