package com.project.capstone.member.repository;

import com.project.capstone.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<String> findByEmail(String email); // 아이디 찾기 할 때 이메일을 통해서 사용자 로그인 아이디를 받아옴
}
