package com.project.capstone.detailSchedule.repository;

import com.project.capstone.bookmark.repository.BookmarkRepository;
import com.project.capstone.detailSchedule.domain.DetailSchedule;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.schedule.domain.Schedule;
import com.project.capstone.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
/**
 * MySQL에서 test 해볼려면, 아래 @SpringBootTest, @Transcational, @Rollback(value = false) 셋 다 있어야함
 */
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
class DetailScheduleRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private DetailScheduleRepository detailScheduleRepository;

    @Test
    @DisplayName("멤버 한명을 추가하고, 그 멤버 객체로 스케줄을 추가하고, 그 스케줄의 일자별 스케줄을 C,R")
    void insertMember_scheduleAndAddDetailSchedules() {

        // given
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.saveAndFlush(member1);

        // when
        Schedule schedule1 = new Schedule("내 여행 1", 12, LocalDate.now(), LocalDate.now().plusDays(2));
        member1.addSchedule(schedule1);

        memberRepository.flush();

        // when
        // 그 스케줄에 일자별 스케줄 추가
        DetailSchedule firstDay_firstLocation = new DetailSchedule(1, 1, "왕왕");
        DetailSchedule secondDay_firstLocation = new DetailSchedule(2, 1, "짬순");
        DetailSchedule secondDay_secondLocation = new DetailSchedule(2, 2, "스엔");
        schedule1.addDetailSchedule(firstDay_firstLocation);
        schedule1.addDetailSchedule(secondDay_firstLocation);
        schedule1.addDetailSchedule(secondDay_secondLocation);

        memberRepository.flush();

        // then
        // member1의 schedules에 schedule1이 들어있다.
        Member foundMember = memberRepository.findById(member1.getId()).get();
        log.info("foundMember = {}", foundMember);
        assertThat(foundMember).isEqualTo(member1);

        // db에 schedule1도 들어있다. (id = 1, member = member1 , name = "여행1", startDateTime = 현재 시각, endDateTime = 현재 시각 + 3)
        Schedule foundSchedule = scheduleRepository.findById(schedule1.getId()).get();
        log.info("foundSchedule = {}", foundSchedule);
        assertThat(foundMember.getSchedules()).contains(schedule1);

        // db에 firstDay_firstLocation, secondDay_firstLocation, secondDay_secondLocation도 들어 있다.
        DetailSchedule foundDetailSchedule1 = detailScheduleRepository.findById(firstDay_firstLocation.getId()).get();
        DetailSchedule foundDetailSchedule2 = detailScheduleRepository.findById(secondDay_firstLocation.getId()).get();
        DetailSchedule foundDetailSchedule3 = detailScheduleRepository.findById(secondDay_secondLocation.getId()).get();

        assertThat(schedule1.getDetailSchedules()).contains(foundDetailSchedule1);
        assertThat(schedule1.getDetailSchedules()).contains(foundDetailSchedule2);
        assertThat(schedule1.getDetailSchedules()).contains(foundDetailSchedule3);
    }

    @Test
    @DisplayName("멤버 한명의 장바구니를 U")
    void updateDetailSchedules() {

        // given
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.saveAndFlush(member1);

        Schedule schedule1 = new Schedule("내 여행 1", 12, LocalDate.now(), LocalDate.now().plusDays(2));
        member1.addSchedule(schedule1);

        DetailSchedule firstDay_firstLocation = new DetailSchedule(1, 1, "왕왕");
        DetailSchedule secondDay_firstLocation = new DetailSchedule(2, 1, "짬순");
        DetailSchedule secondDay_secondLocation = new DetailSchedule(2, 2, "스엔");
        schedule1.addDetailSchedule(firstDay_firstLocation);
        schedule1.addDetailSchedule(secondDay_firstLocation);
        schedule1.addDetailSchedule(secondDay_secondLocation);

        memberRepository.flush();

        // when
        

        // then
        // 멤버 객체의 북마크가 수정 되었다.
        // 북마크 테이블의 북마크가 수정 되었다.
    }

    @Test
    @DisplayName("멤버 한명의 장바구니를 D")
    void deleteDetailSchedules(){

        // given
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.saveAndFlush(member1);

        Schedule schedule1 = new Schedule("내 여행 1", 12, LocalDate.now(), LocalDate.now().plusDays(2));
        member1.addSchedule(schedule1);

        DetailSchedule firstDay_firstLocation = new DetailSchedule(1, 1, "왕왕");
        DetailSchedule secondDay_firstLocation = new DetailSchedule(2, 1, "짬순");
        DetailSchedule secondDay_secondLocation = new DetailSchedule(2, 2, "스엔");
        schedule1.addDetailSchedule(firstDay_firstLocation);
        schedule1.addDetailSchedule(secondDay_firstLocation);
        schedule1.addDetailSchedule(secondDay_secondLocation);

        memberRepository.flush();

        // when
        // 멤버의 북마크 중 하나를 삭제

        // then
        // 멤버 객체의 북마크가 삭제 되었다.
        // 북마크 테이블의 북마크가 삭제 되었다.
    }

    @Test
    @DisplayName("멤버 한명을 삭제했을 때, 장바구니도 D")
    void deleteDetailSchedules_(){
    }
}