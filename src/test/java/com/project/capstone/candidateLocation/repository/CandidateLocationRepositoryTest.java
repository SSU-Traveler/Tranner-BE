package com.project.capstone.candidateLocation.repository;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
/**
 * MySQL에서 test 해볼려면, 아래 @SpringBootTest, @Transcational, @Rollback(value = false) 셋 다 있어야함
 */
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
class CandidateLocationRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CandidateLocationRepository candidateLocationRepository;

    @Test
    @DisplayName("멤버 한명을 추가하고, 그 멤버 객체로 장바구니를 C,R")
    void insertMemberAndAddCandidateLocation() {

        // given
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.saveAndFlush(member1);

        // when
        CandidateLocation candidateLocation1 = new CandidateLocation("강서 힐스테이트");
        CandidateLocation candidateLocation2 = new CandidateLocation("목동");
        List<CandidateLocation> candidateLocations = new ArrayList<>();
        candidateLocations.add(candidateLocation1);
        candidateLocations.add(candidateLocation2);

        member1.addCandidateLocations(candidateLocations);
        
        memberRepository.flush();

        // then
        // db에 candidateLocation1이 들어있다.
        CandidateLocation foundCandidateLocation1 = candidateLocationRepository.findById(candidateLocation1.getId()).get();
        log.info("DB에 저장된 장바구니 장소 = {}", foundCandidateLocation1);
        assertThat(foundCandidateLocation1).isEqualTo(candidateLocation1);

        // db에 candidateLocation2이 들어있다.
        CandidateLocation foundCandidateLocation2 = candidateLocationRepository.findById(candidateLocation2.getId()).get();
        log.info("DB에 저장된 장바구니 장소 = {}", foundCandidateLocation2);
        assertThat(foundCandidateLocation2).isEqualTo(candidateLocation2);

        // db에 저장된 member1에도 candidateLocation1이 들어있다.
        Member foundMember = memberRepository.findById(member1.getId()).get();
        log.info("DB에 저장된 멤버 = {}", foundMember);
        assertThat(foundMember).isEqualTo(member1);
    }

    @Test
    @DisplayName("멤버 한명의 장바구니를 U") // 우리가 장바구니 장소를 수정할 일은 없다. 단지 삭제할 뿐
    void updateCandidateLocation() {}

    @Test
    @DisplayName("멤버 한명의 장바구니를 D")
    void deleteCandidateLocation(){

        // given
        // 멤버 한명이 있다.
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.save(member1);

        // 그 멤버는 장바구니가 있다.
        CandidateLocation candidateLocation1 = new CandidateLocation("땅호리 집");
        member1.addCandidateLocation(candidateLocation1);

        CandidateLocation candidateLocation2 = new CandidateLocation("목동");
        member1.addCandidateLocation(candidateLocation2);

        memberRepository.flush();

        // when
        // 멤버의 장바구니 중 하나를 삭제
        Member updateMember = memberRepository.findByUsername("member1");
        updateMember.deleteCandidateLocation(candidateLocation2);

        memberRepository.flush();


        // then
        // 멤버 객체의 북마크가 삭제 되었다.
        Member foundMember = memberRepository.findByUsername("member1");
        log.info("DB에 저장된 멤버 = {}", foundMember);

        // 북마크 테이블의 북마크가 삭제 되었다.
        assertThrows(NoSuchElementException.class,()->{
            candidateLocationRepository.findById(candidateLocation2.getId()).get();
        });
    }

    @Test
    @DisplayName("멤버 한명을 삭제했을 때, 장바구니도 D")
    void deleteMember() {

        // given
        // 멤버 한명이 있다.
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.save(member1);

        // 그 멤버는 장바구니가 있다.
        CandidateLocation candidateLocation1 = new CandidateLocation("땅호리 집");
        member1.addCandidateLocation(candidateLocation1);

        CandidateLocation candidateLocation2 = new CandidateLocation("목동");
        member1.addCandidateLocation(candidateLocation2);

        memberRepository.flush();

        // when
        // 멤버를 삭제했다.
        memberRepository.delete(member1);
        memberRepository.flush();

        // then
        // 그 멤버의 북마크 2개가 사라졌다.
        assertThrows(NoSuchElementException.class,()->{
            candidateLocationRepository.findById(candidateLocation1.getId()).get();
        });
        assertThrows(NoSuchElementException.class,()->{
            candidateLocationRepository.findById(candidateLocation2.getId()).get();
        });
    }
}