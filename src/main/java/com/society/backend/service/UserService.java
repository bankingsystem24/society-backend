package com.society.backend.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.society.backend.dto.UserRequest;
import com.society.backend.dto.UserResponse;
import com.society.backend.entity.User;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.MemberRepository;
import com.society.backend.repository.SocietyRepository;
import com.society.backend.repository.UserRepository;
import com.society.backend.entity.Member;
import com.society.backend.entity.Society;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SocietyRepository societyRepository;

    public User save(User user) {

        // 🔥 FIX MEMBER
        if (user.getMember() != null && user.getMember().getId() != null) {

            Long memberId = user.getMember().getId();

            Member m = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            user.setMember(m); // ✅ IMPORTANT FIX
        } else {
            user.setMember(null);
        }
        User saved = userRepository.save(user);
        userRepository.flush(); // 🔥 FORCE DB WRITE (IMPORTANT DEBUG STEP)
        return saved;
    }

    public User createUser(UserRequest req) {

        User user = new User();

        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        user.setMobile(req.getMobile());
        user.setRole(req.getRole());
        user.setActive(req.getActive());

        // MEMBER
        if (req.getMember() != null && req.getMember().getId() != null) {
            Member m = memberRepository.findById(req.getMember().getId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            user.setMember(m);
        }

        // SOCIETY
        if (req.getSociety() != null && req.getSociety().getId() != null) {
            Society s = societyRepository.findById(req.getSociety().getId())
                    .orElseThrow(() -> new RuntimeException("Society not found"));
            user.setSociety(s);
        }

        return userRepository.save(user);
    }


    public List<UserResponse> getAll(Long societyId) {

        if (societyId != null) {
            return userRepository.findBySocietyId(societyId)
                    .stream()
                    .map(this::toResponse)
                    .toList();
        } else {
            return userRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }
    }

    private UserResponse toResponse(User user) {
        UserResponse res = new UserResponse();

        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setMobile(user.getMobile());
        res.setRole(user.getRole());
        res.setActive(user.getActive());

        // Society mapping
        if (user.getSociety() != null) {
            res.setSocietyId(user.getSociety().getId());
            res.setSocietyName(user.getSociety().getSocietyName());
        }

        // Member mapping (if exists in entity)
        if (user.getMember() != null) {
            res.setMemberId(user.getMember().getId());
            res.setMemberName(user.getMember().getName());
        }

        return res;
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

    public void updateStatus(Long id, Boolean active) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setActive(active);
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
