package com.project.capstone.member.repository;

import com.project.capstone.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@DataJpaTest
/**
 * MySQL에서 test 해볼려면, 아래 @SpringBootTest, @Transcational, @Rollback(value = false) 셋 다 있어야함
 */
//@SpringBootTest
//@Transactional
// @Rollback(value = false
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @Test
    @DisplayName("멤버 한명을 C,R")
    void insert(){

        // given
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        repository.saveAndFlush(member1);
        log.info("저장할 멤버 = {}",member1);

        // when
        Member foundMember = repository.findById(member1.getId()).get();
        log.info("DB에서 찾은 멤버 = {}",foundMember);

        // then
        assertThat(foundMember).isEqualTo(foundMember);
    }

    @Test
    @DisplayName("멤버 한명을 U")
    void update() {

        // given
        Member savedMember = new Member("문상준", "1111", "member1", "1@gmail.com", LocalDate.now(), null);
        repository.saveAndFlush(savedMember);
        log.info("변경 전 닉네임 = {}", savedMember.getNickname());

        // when
        Member updatedMember=repository.findById(savedMember.getId()).get();
        updatedMember.changeNickname("강민석 ㅄ");

        // then
        updatedMember = repository.findById(savedMember.getId()).get();
        log.info("변경 후 닉네임 = {}", updatedMember.getNickname());
        assertThat(updatedMember.getNickname()).isEqualTo(updatedMember.getNickname());
    }

    @Test
    @DisplayName("멤버 한명을 D")
    void delete() {

        // given
        Member savedMember = new Member("문상준", "1111", "member1", "1@gmail.com", LocalDate.now(), null);
        repository.save(savedMember);

        // when
        repository.delete(savedMember);
//        log.info("{}",repository.findById(savedMember.getId()).get());
        // then
        assertThrows(NoSuchElementException.class, () -> {
            repository.findById(savedMember.getId()).get();
        });
    }
}