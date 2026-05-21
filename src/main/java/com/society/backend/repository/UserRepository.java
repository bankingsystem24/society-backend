package com.society.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findBySocietyId(Long societyId);
    
}
