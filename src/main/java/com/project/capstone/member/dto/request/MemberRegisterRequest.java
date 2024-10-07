package com.project.capstone.member.dto.request;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public record MemberRegisterRequest(
        @NotBlank(message = "아이디를 입력해주세요")
        String username,
        @NotBlank(message = "비밀번호를 입력해주세요")
        String password,
        @Email(message = "유효한 이메일 주소를 입력해주세요")
        @NotBlank(message = "이메일을 입력해주세요")
        String memberEmail,
        @NotBlank(message = "닉네임을 입력해주세요")
        String nickname) { //request에서 json객체를 담을 클래스
}
