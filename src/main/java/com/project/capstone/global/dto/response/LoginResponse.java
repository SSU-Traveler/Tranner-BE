package com.project.capstone.global.dto.response;

import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import com.project.capstone.schedule.dto.response.ListScheduleResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LoginResponse<T> {
    @Schema(description = "장바구니 리스트 데이터")
    private List<ListScheduleResponse> candidateLocation;
    private String refreshToken;
    private String accessToken;

    public LoginResponse(List<CandidateLocationResponse> candidateLocationList, String accessToken, String refreshToken)
    {
        this.candidateLocation = candidateLocation;
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
    }
}

