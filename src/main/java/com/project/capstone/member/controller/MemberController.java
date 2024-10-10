package com.project.capstone.member.controller;

import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.dto.request.MemberEditRequest;
import com.project.capstone.member.dto.request.MemberLoginRequest;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
//import com.project.capstone.member.service.MemberService;
import com.project.capstone.member.dto.response.MemberEditPageResponse;
import com.project.capstone.member.dto.response.MypageResponse;
import com.project.capstone.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody MemberRegisterRequest request) {
        memberService.register(request);
        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }

    @GetMapping("/mypage")
    public ResponseEntity<MypageResponse> mypage(HttpServletRequest request) {
        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);
        log.info("마이페이지를 요청한 username은 = {}", username);
        MypageResponse mypageResponse = memberService.getMyPage(username);

        return ResponseEntity.ok().body(mypageResponse);
    }

    @GetMapping
    public ResponseEntity<MemberEditPageResponse> edit(HttpServletRequest request){
        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        log.info("마이페이지에서 프로필 수정 페이지를 요청한 username은 = {}",username);

        MemberEditPageResponse memberEditPageResponse = memberService.getMemberEditPage(username);

        return ResponseEntity.ok().body(memberEditPageResponse);
    }

    @PatchMapping
    public ResponseEntity<Void> edit(HttpServletRequest request,
                       @Validated @RequestBody MemberEditRequest memberEditRequest) {

        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        log.info("마이페이지에서 프로플 수정을 요청한 username은 = {}", username);

        memberService.editMember(username, memberEditRequest);


        return ResponseEntity.status(HttpStatus.FOUND) // 302 리다이렉션
                .location(URI.create("/member")) // 리다이렉트할 URL
                .build();
    }
}
