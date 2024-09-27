package com.project.capstone.member.controller;

import com.project.capstone.member.dto.request.MemberLoginRequest;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
//import com.project.capstone.member.service.MemberService;
import com.project.capstone.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public String register(@RequestBody @Valid MemberRegisterRequest request) {
        memberService.register(request);
        return "ok";
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        return "로그인 성공";
    }

}
