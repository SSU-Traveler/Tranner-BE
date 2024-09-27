package com.project.capstone.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken { // JWT의 DTO
    private String grantType; //JWT에 대한 인증 타입
    private String accessToken; // 인증을 위한 토큰 -> 사용자 권한 정보, 만료 시간 담겨있음
    private String refreshToken; // accessToken의 유효기간이 끝났을 때 재발급을 위한 토큰
}