package com.society.backend.service;

import com.society.backend.dto.UserRequest;
import com.society.backend.dto.UserResponse;
import com.society.backend.entity.Member;
import com.society.backend.entity.Society;
import com.society.backend.entity.User;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.MemberRepository;
import com.society.backend.repository.SocietyRepository;
import com.society.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final SocietyRepository societyRepository;

    public UserService(UserRepository userRepository, 
                        MemberRepository memberRepository,
                        SocietyRepository societyRepository){
            this.userRepository = userRepository;
            this.memberRepository = memberRepository;
            this.societyRepository = societyRepository;

    };

    // =========================
    // CREATE USER
    // =========================
    public UserResponse createUser(UserRequest req) {

        User user = new User();

        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword()); 
        user.setEmail(req.getEmail());
        user.setMobile(req.getMobile());
        user.setRole(req.getRole());
        user.setActive(req.getActive());
        user.setName(req.getName());

        // =========================
        // MEMBER
        // =========================
        if (req.getMember() != null && req.getMember().getId() != null) {

            Member member = memberRepository.findById(req.getMember().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

            user.setMember(member);
        }

        if (req.getSociety() != null
                && req.getSociety().getId() != null
                && req.getSociety().getId() > 0) {

            Society society = societyRepository.findById(req.getSociety().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Society not found"));

            user.setSociety(society);
        }

        try {
            User saved = userRepository.save(user);
            return toResponse(saved);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        // User saved = userRepository.save(user);
        // return toResponse(saved);
    }

    // =========================
    // GET ALL
    // =========================
    public List<UserResponse> getAll(Long societyId) {

        List<User> users;

        if (societyId != null) {
            users = userRepository.findBySociety_Id(societyId);
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // GET BY ID
    // =========================
    public UserResponse getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        return toResponse(user);
    }

    // =========================
    // UPDATE USER
    // =========================
    public UserResponse update(Long id, UserRequest req) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setMobile(req.getMobile());
        user.setRole(req.getRole());
        user.setActive(req.getActive());
        user.setName(req.getName());
        // MEMBER
        if (req.getMember() != null && req.getMember().getId() != null) {
            Member member = memberRepository.findById(req.getMember().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
            user.setMember(member);
        }

        // SOCIETY
        if (req.getSociety() != null && req.getSociety().getId() != null) {
            Society society = societyRepository.findById(req.getSociety().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Society not found"));
            user.setSociety(society);
        }

        User updated = userRepository.save(user);
        return toResponse(updated);
    }

    // =========================
    // UPDATE STATUS
    // =========================
    public void updateStatus(Long id, Boolean active) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setActive(active);
        userRepository.save(user);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // =========================
    // MAPPER
    // =========================
    private UserResponse toResponse(User user) {

        UserResponse res = new UserResponse();

        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setMobile(user.getMobile());
        res.setRole(user.getRole());
        res.setActive(user.getActive());
        res.setName(user.getName());

        // SOCIETY SAFE
        if (user.getSociety() != null) {
            res.setSocietyId(user.getSociety().getId());
            res.setSocietyName(user.getSociety().getSocietyName());
        }

        // MEMBER SAFE
        if (user.getMember() != null) {
            res.setMemberId(user.getMember().getId());
            res.setMemberName(user.getMember().getName());
        }

        return res;
    }

public UserResponse updateFromRequest(Long id, UserRequest req) {

    try {
    } catch (Exception e) {
        e.printStackTrace();
    }

    User user = userRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

    // =========================
    // BASIC FIELDS
    // =========================
    user.setUsername(req.getUsername());
    user.setPassword(req.getPassword()); // Later use BCrypt
    user.setEmail(req.getEmail());
    user.setMobile(req.getMobile());
    user.setRole(req.getRole());
    user.setActive(req.getActive());
    user.setName(req.getName());
    // =========================
    // MEMBER
    // =========================
    if (req.getMember() != null &&
            req.getMember().getId() != null) {

        Member member = memberRepository.findById(req.getMember().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Member not found"));

        user.setMember(member);

    } else {
        user.setMember(null);
    }

    // =========================
    // SOCIETY
    // =========================
    Long societyId = (req.getSociety() != null)
            ? req.getSociety().getId()
            : null;

    if (societyId == null || societyId == 0) {
        user.setSociety(null);
    } else {
        Society society = societyRepository.findById(societyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Society not found"));

        user.setSociety(society);
    }

    User updated = userRepository.save(user);

    return toResponse(updated);
}

}