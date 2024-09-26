package com.project.capstone.member.repository;

import com.project.capstone.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsById(String id);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
