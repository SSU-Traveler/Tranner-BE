package com.project.capstone.schedule.repository;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    // username으로 해당 유저의 모든 스케줄을 조회하는 메소드
    List<Schedule> findAllByMember_Username(String username);
    
    List<Schedule> findAllById(Long id);
}
