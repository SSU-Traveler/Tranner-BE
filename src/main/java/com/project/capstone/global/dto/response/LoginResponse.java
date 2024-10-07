package com.project.capstone.global.dto.response;

import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.ListScheduleResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse<T> {
    @Schema(description = "장바구니 리스트 데이터")
    private List<ListScheduleResponse> candidateLocation;

    public LoginResponse(List<ListScheduleResponse> candidateLocation) {
        this.candidateLocation = candidateLocation;
    }
}
