package com.project.capstone.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEditRequest {

    @NotEmpty
    String nickname;

    public MemberEditRequest() {
    }

    public MemberEditRequest(String nickname) {
        this.nickname = nickname;
    }
}
