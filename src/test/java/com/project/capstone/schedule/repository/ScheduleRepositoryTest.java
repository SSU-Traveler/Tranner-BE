package com.project.capstone.schedule.repository;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.schedule.domain.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
class ScheduleRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("멤버 한명을 추가하고, 그 멤버 객체로 스케줄을 C,R")
    void insertMemberAndAddSchedule() {

        // given
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.saveAndFlush(member1);

        // when
        Schedule schedule1 = new Schedule("내 여행 1", 12, LocalDate.now(), LocalDate.now().plusDays(2));
        member1.addSchedule(schedule1);

        memberRepository.flush();

        // then
        // member1의 schedules에 schedule1이 들어있다.
        Member foundMember = memberRepository.findById(member1.getId()).get();
        log.info("DB에 저장된 멤버의 여행 정보 = {}", foundMember.getSchedules());
        assertThat(foundMember.getSchedules()).contains(schedule1);

        // db에 schedule1도 들어있다. (id = 1, member = member1 , name = "내 여행 1", startDateTime = 현재 시각, endDateTime = 현재 시각 + 2)
        Schedule foundSchedule = scheduleRepository.findById(schedule1.getId()).get();
        log.info("DB에 저장된 여행 정보 = {}", foundSchedule);
        assertThat(foundSchedule).isEqualTo(schedule1);
    }

    @Test
    @DisplayName("멤버 한명의 스케줄을 U")
    void updateSchedule() {

        // given
        // 멤버 한명이 있다.
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.saveAndFlush(member1);

        // 그 멤버는 스케줄이 있다.
        Schedule schedule1 = new Schedule("내 여행 1", 12, LocalDate.now(), LocalDate.now().plusDays(2));
        member1.addSchedule(schedule1);

        memberRepository.flush();

        // when
        // 멤버의 스케줄 중 하나를 수정
        Schedule editSchedule = scheduleRepository.findById(schedule1.getId()).get();
        editSchedule.editName("내 수정된 여행 1");
        editSchedule.editHowManyPeople(4);
        editSchedule.editDate(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));

        scheduleRepository.flush();

        // then
        // 멤버 객체의 북마크가 수정 되었다.
        Member foundMember = memberRepository.findById(member1.getId()).get();
        log.info("DB에 저장된 멤버 정보 = {}", foundMember);
        assertThat(foundMember.getSchedules()).contains(schedule1);

        // 북마크 테이블의 북마크가 수정 되었다.
        Schedule foundSchedule = scheduleRepository.findById(schedule1.getId()).get();
        log.info("DB에 저장된 여행 정보 = {}", foundSchedule);
        assertThat(foundSchedule).isEqualTo(schedule1);
    }

    @Test
    @DisplayName("멤버 한명의 스케줄을 D")
    void deleteSchedule(){

        // given
        // 멤버 한명이 있다.
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.saveAndFlush(member1);

        // 그 멤버는 스케줄이 있다.
        Schedule schedule1 = new Schedule("내 여행 1", 12, LocalDate.now(), LocalDate.now().plusDays(2));
        member1.addSchedule(schedule1);

        memberRepository.flush();

        // when
        // 멤버에서 스케줄 하나를 지운다.
        member1.deleteSchedule(schedule1);
        memberRepository.flush();

        // then
        // 멤버 객체의 스케줄이 삭제 되었다.
        Member foundMember = memberRepository.findById(member1.getId()).get();
        log.info("DB에 저장된 멤버 = {}", foundMember);

        // 북마크 테이블의 스케줄이 삭제 되었다.
        assertThrows(NoSuchElementException.class,()->{
            scheduleRepository.findById(schedule1.getId()).get();
        });
    }

    @Test
    @DisplayName("멤버 한명을 삭제했을 때, 스케줄도 D")
    void deleteMember(){

        // given
        // 멤버 한명이 있다.
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.save(member1);

        // 그 멤버는 스케줄이 있다.
        Schedule schedule1 = new Schedule("내 여행 1", 12, LocalDate.now(), LocalDate.now().plusDays(2));
        member1.addSchedule(schedule1);

        memberRepository.flush();

        // when
        // 멤버를 삭제했다.
        memberRepository.delete(member1);
        memberRepository.flush();

        // then
        // 그 멤버의 스케줄이 사라졌다.
        assertThrows(NoSuchElementException.class,()->{
            scheduleRepository.findById(schedule1.getId()).get();
        });
    }
}