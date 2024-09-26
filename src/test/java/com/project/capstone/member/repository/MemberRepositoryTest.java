package com.project.capstone.member.repository;

import com.project.capstone.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // JPA 관련 테스트 설정
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional // 테스트 종료 후 롤백
    public void testSaveMember() {
        // Given: 새로운 회원 정보
        Member member = new Member();
        member.setId("testUser");
        member.setPw("password123");
        member.setEmail("test@example.com");
        member.setNickname("nickname");

        // When: 회원 정보를 저장
        Member savedMember = memberRepository.save(member);

        // Then: 저장된 회원 정보가 데이터베이스에 잘 들어갔는지 확인
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getId()).isEqualTo("testUser");
        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");
        assertThat(savedMember.getNickname()).isEqualTo("nickname");
    }

    @Test
    @Transactional
    public void testExistsById() {
        // Given: 새로운 회원 정보
        Member member = new Member();
        member.setId("testUser");
        member.setPw("password123");
        member.setEmail("test@example.com");
        member.setNickname("nickname");
        memberRepository.save(member); // 회원 정보를 먼저 저장

        // When: ID로 회원 존재 여부 확인
        boolean exists = memberRepository.existsById("testUser");

        // Then: 회원이 존재하는지 확인
        assertThat(exists).isTrue();
    }

    @Test
    @Transactional
    public void testExistsByNickname() {
        // Given: 새로운 회원 정보
        Member member = new Member();
        member.setId("testUser");
        member.setPw("password123");
        member.setEmail("test@example.com");
        member.setNickname("nickname");
        memberRepository.save(member); // 회원 정보를 먼저 저장

        // When: 닉네임으로 회원 존재 여부 확인
        boolean exists = memberRepository.existsByNickname("nickname");

        // Then: 회원이 존재하는지 확인
        assertThat(exists).isTrue();
    }

    @Test
    @Transactional
    public void testExistsByEmail() {
        // Given: 새로운 회원 정보
        Member member = new Member();
        member.setId("testUser");
        member.setPw("password123");
        member.setEmail("test@example.com");
        member.setNickname("nickname");
        memberRepository.save(member); // 회원 정보를 먼저 저장

        // When: 이메일로 회원 존재 여부 확인
        boolean exists = memberRepository.existsByEmail("test@example.com");

        // Then: 회원이 존재하는지 확인
        assertThat(exists).isTrue();
    }
}
