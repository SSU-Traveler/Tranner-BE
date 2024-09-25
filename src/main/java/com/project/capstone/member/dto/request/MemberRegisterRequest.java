package com.project.capstone.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberRegisterRequest(
        @NotBlank
        String id,
        @NotBlank
        String pw,
        @Email
        @NotBlank
        String memberEmail,
        @NotBlank
        String nickname) { //request에서 json객체를 담을 클래스
}
