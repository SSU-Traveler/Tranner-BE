package com.project.capstone.member.repository;

import com.project.capstone.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String memberId); // 아이디로 멤버 조회
    Boolean existsByUsername(String username);
}
