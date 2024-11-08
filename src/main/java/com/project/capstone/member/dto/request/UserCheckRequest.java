package com.project.capstone.member.dto.request;

import org.hibernate.validator.constraints.NotBlank;

public record UserCheckRequest(
        @NotBlank(message = "아이디를 입력해주세요")
        String username){ }
