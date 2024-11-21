package com.project.capstone.schedule.controller;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.member.service.CustomMemberDetailService;
import com.project.capstone.schedule.dto.request.AddScheduleRequest;
import com.project.capstone.schedule.dto.request.EditScheduleRequest;
import com.project.capstone.schedule.dto.request.GetAddSchedule;
import com.project.capstone.schedule.dto.response.FindScheduleDTO;
import com.project.capstone.schedule.dto.response.ListScheduleResponse;
import com.project.capstone.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.project.capstone.bookmark.repository.BookmarkRepository;
import java.util.List;

@RestController
@RequestMapping("/schedule")
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final CustomMemberDetailService customMemberDetailService;
    private final JwtUtil jwtUtil;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    //북마크 업데이트

    //저장된 북마크 정보 가져오기
    @GetMapping("/getbookmarks")
    public ResponseEntity<List<Bookmark>> CreateSchedule(HttpServletRequest request, @RequestBody GetAddSchedule locations) {
        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        Member member = memberRepository.findByUsername(username);
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMemberId(member.getId());
        ListScheduleResponse listScheduleResponse =  scheduleService.AddCandidateLocation(locations, username);

        return ResponseEntity.ok().body(bookmarks);
    }

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

    // 기존 스케줄 수정
    @PostMapping("/edit")
    public ResponseEntity<String> editSchedule(HttpServletRequest request,
                                               @Validated @RequestBody EditScheduleRequest scheduleRequest){

        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        long scheduleId = Long.parseLong(request.getParameter("scheduleId"));

        log.info("스케줄 수정 요청을 한 멤버 = {}",username);
        log.info("스케줄 수정 요청을 통해 넘어온 정보 = {}", scheduleRequest);

        scheduleService.editSchedule(username,scheduleId,scheduleRequest);

        List<FindScheduleDTO> allSchedules = scheduleService.findAllSchedules(username);
        log.info("스케줄 변경 이후 {}의 스케줄들 = {}", username, allSchedules);

        return ResponseEntity.ok("스케줄 수정 성공");
    }
}