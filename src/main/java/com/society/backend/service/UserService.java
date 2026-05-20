package com.society.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.UserResponse;
import com.society.backend.entity.User;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
    
    public List<UserResponse> getAll() {

        return userRepository.findAll().stream().map(user -> {

            UserResponse res = new UserResponse();

            res.setId(user.getId());
            res.setUsername(user.getUsername());
            res.setEmail(user.getEmail());
            res.setMobile(user.getMobile());
            res.setRole(user.getRole());
            res.setActive(user.getActive());

            if (user.getMember() != null) {

                res.setMemberId(user.getMember().getId());
                res.setMemberName(user.getMember().getName());

                if (user.getMember().getFlat() != null &&
                    user.getMember().getFlat().getSociety() != null) {

                    res.setSocietyId(user.getMember().getFlat().getSociety().getId());
                    res.setSocietyName(user.getMember().getFlat().getSociety().getSocietyName());
                }
            }

            return res;

        }).toList();
    }

    public UserResponse getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        UserResponse res = new UserResponse();

        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setMobile(user.getMobile());
        res.setRole(user.getRole());
        res.setActive(user.getActive());

        return res;
    }

    public User update(Long id, User user) {

        User existing = userRepository.findById(id).orElse(null);

        if (existing != null) {

            existing.setUsername(user.getUsername());
            existing.setPassword(user.getPassword());
            existing.setEmail(user.getEmail());
            existing.setMobile(user.getMobile());
            existing.setRole(user.getRole());
            existing.setMember(user.getMember());
            existing.setActive(user.getActive());
            existing.setSociety(user.getSociety());
         

            return userRepository.save(existing);
        }

        return null;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
