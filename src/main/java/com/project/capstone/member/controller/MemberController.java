package com.project.capstone.member.controller;

import com.project.capstone.member.dto.request.MemberRegisterRequest;
import com.project.capstone.member.dto.response.MemberResponse;
import com.project.capstone.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<MemberResponse>registerMember(@Valid @RequestBody MemberRegisterRequest request){
        MemberResponse response = memberService.registerMember(request);
        return new ResponseEntity<>(response,response.isSuccess()? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

}
