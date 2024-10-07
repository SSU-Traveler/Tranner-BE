package com.project.capstone.schedule.controller;

import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.service.CustomMemberDetailService;
import com.project.capstone.schedule.dto.request.GetAddSchedule;
import com.project.capstone.schedule.dto.request.Location;
import com.project.capstone.schedule.dto.response.ListScheduleResponse;
import com.project.capstone.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final CustomMemberDetailService customMemberDetailService;
    private final JwtUtil jwtUtil;

    @GetMapping("/add")
    public ResponseEntity<ListScheduleResponse> CreateSchedule(HttpServletRequest request, @RequestBody GetAddSchedule locations) {
        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        ListScheduleResponse listScheduleResponse =  scheduleService.AddCandidateLocation(locations, username);
        return ResponseEntity.ok().body(listScheduleResponse);
    }


}
