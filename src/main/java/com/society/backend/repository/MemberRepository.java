package com.society.backend.repository;

import com.society.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

List<Member> findBySociety_Id(Long societyId);



}