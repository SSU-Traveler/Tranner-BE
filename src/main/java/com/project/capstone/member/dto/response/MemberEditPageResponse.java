package com.project.capstone.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEditPageResponse {
    String nickname;
    String email;

    public MemberEditPageResponse(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
