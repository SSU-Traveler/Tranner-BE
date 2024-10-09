package com.project.capstone.member.repository;

import com.project.capstone.member.domain.Member;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username); // 아이디로 멤버 조회

    Boolean existsByUsername(String username);

    Optional<Member> findByEmail(String email); // 이메일로 멤버 아이디 조회

    Boolean existsByEmail(String email);
}
