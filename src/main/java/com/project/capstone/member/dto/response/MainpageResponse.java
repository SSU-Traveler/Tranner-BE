package com.project.capstone.member.dto.response;

import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MainpageResponse<T> {
    @Schema(description = "장바구니 리스트 데이터")
    private List<CandidateLocationResponse> candidateLocation;

    public MainpageResponse(List<CandidateLocationResponse> candidateLocation) {
        this.candidateLocation = candidateLocation;
    }
}
