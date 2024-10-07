package com.project.capstone.schedule.repository;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findAllById(Long id);
}
