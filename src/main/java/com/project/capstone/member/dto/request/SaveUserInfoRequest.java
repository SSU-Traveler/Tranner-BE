package com.project.capstone.member.dto.request;

import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaveUserInfoRequest {
    @Schema(description = "장바구니 리스트 데이터")
    private List<CandidateLocationRequest> candidateLocations;

    @Schema(description = "찜한 장소 리스트 데이터")
    private List<BookmarkRequest> bookmarks;

    public SaveUserInfoRequest(List<CandidateLocationRequest> candidateLocations,
                               List<BookmarkRequest> bookmarks) {
        this.candidateLocations = candidateLocations;
        this.bookmarks = bookmarks;
    }
}
