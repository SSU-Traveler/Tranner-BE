package com.project.capstone.global.dto.response;

import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import com.project.capstone.schedule.dto.response.ListScheduleResponse;
import com.project.capstone.schedule.dto.response.ScheduleResponse;
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
    private List<CandidateLocationResponse> candidateLocation;
    private List<BookmarkResponse> bookmark;
    private String accessToken;
    private String refreshToken;
    private String username;
    private String nickname;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;

    public LoginResponse(List<CandidateLocationResponse> candidateLocation,List<BookmarkResponse> bookmark ,String accessToken, String refreshToken,String username , String nickname
    ,int accessTokenExpiration, int refreshTokenExpiration)
    {
        this.candidateLocation = candidateLocation;
        this.bookmark=bookmark;
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
        this.username=username;
        this.nickname=nickname;
        this.accessTokenExpiration=accessTokenExpiration;
        this.refreshTokenExpiration=refreshTokenExpiration;
    }
}

