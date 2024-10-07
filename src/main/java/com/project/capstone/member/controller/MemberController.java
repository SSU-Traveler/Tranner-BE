package com.project.capstone.member.controller;

import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.dto.request.MemberLoginRequest;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
//import com.project.capstone.member.service.MemberService;
import com.project.capstone.member.dto.response.MypageResponse;
import com.project.capstone.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
