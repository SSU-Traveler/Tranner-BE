package com.project.capstone.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank
        String username,
        @NotBlank
        String password) {
}
