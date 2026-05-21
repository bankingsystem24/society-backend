package com.society.backend.repository;

import com.society.backend.entity.Member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findBySocietyId(Long societyId);
}