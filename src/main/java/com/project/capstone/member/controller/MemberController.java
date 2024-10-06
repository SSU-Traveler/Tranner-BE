package com.project.capstone.member.controller;

import com.project.capstone.member.dto.request.MemberLoginRequest;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
//import com.project.capstone.member.service.MemberService;
import com.project.capstone.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody MemberRegisterRequest request) {
        memberService.register(request);
        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }

    @PostMapping("/admin")
    public String admin() {
        return "jwt 검증 완료";
    }
}
