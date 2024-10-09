package com.project.capstone.member.dto.response;

import lombok.Data;

@Data
public class MemberResponse {
    private boolean success; // 가입 성공 여부
    private String message;  // 상태 메시지
    private Long memberId;   // (선택적) 생성된 회원의 ID

    // 생성자
    public MemberResponse(boolean success, String message, Long memberId) {
        this.success = success;
        this.message = message;
        this.memberId = memberId;
    }
}