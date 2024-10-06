package com.project.capstone.schedule.controller;

import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.schedule.domain.Schedule;
import com.project.capstone.schedule.dto.request.AddScheduleRequest;
import com.project.capstone.schedule.dto.response.FindScheduleDTO;
import com.project.capstone.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {

    private final JwtUtil jwtUtil;
    private final ScheduleService scheduleService;

    // 신규 스케줄 생성
    @PostMapping("/add")
    public ResponseEntity<String> addSchedule(HttpServletRequest request,
                                              @Validated @RequestBody AddScheduleRequest scheduleRequest) {

        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        log.info("스케줄 생성 요청을 한 멤버 = {}", username);
        log.info("스케줄 생성 요청을 통해 넘어온 정보 = {}", scheduleRequest);

        scheduleService.saveSchedule(username, scheduleRequest);

        return ResponseEntity.ok("스케줄 생성 성공");
    }
}
