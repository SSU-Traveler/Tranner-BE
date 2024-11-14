package com.project.capstone.global.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.global.Service.KakaoService;
import com.project.capstone.global.dto.KakaoDTO;
import com.project.capstone.global.dto.response.LoginResponse;
import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
import com.project.capstone.member.service.MemberService;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    // 카카오 로그인 토큰 프론트로부터 받기
    @PostMapping("/kakaoLogin")
    public void kakaoLogin(@RequestBody Map<String, String> payload, HttpServletResponse response) throws Exception {

        String code = payload.get("code");
        log.info("카카오 코드 = {}", code);

        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(code);
        log.info("kakao 토큰 정보 = {}", kakaoInfo);

        // 회원가입 로직
        try {
            MemberRegisterRequest request = new MemberRegisterRequest(Long.toString(kakaoInfo.getId()), UUID.randomUUID().toString(), kakaoInfo.getEmail(), kakaoInfo.getNickname());
            memberService.register(request);
        } catch (Exception e) {
            log.info("해당 이메일로 이미 가입된 회원입니다");
        }

        kakaoService.makeAndSendJwtToken(kakaoInfo, response);
    }

}
