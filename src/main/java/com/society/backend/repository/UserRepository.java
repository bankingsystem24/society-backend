package com.society.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
