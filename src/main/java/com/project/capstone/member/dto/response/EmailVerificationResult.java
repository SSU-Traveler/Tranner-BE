package com.project.capstone.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class EmailVerificationResult {

    public EmailVerificationResult(String message) {
        this.message = message;
    }

    private final String message; // 인증 결과 메시지
    private boolean verified;

     // 인증 성공 여부를 반환하는 메서드

    public boolean isVerified() {
        return this.verified;
    }

    // 인증 성공 여부를 설정하는 메서드
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    // 새로운 생성자 추가: 인증 성공 여부를 포함
    public EmailVerificationResult(String message, boolean verified) {
        this.message = message;
        this.verified = verified;
    }
}