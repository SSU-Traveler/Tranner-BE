package com.project.capstone.schedule.dto.response;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListScheduleResponse<T> {
    @Schema(description = "장바구니 리스트 데이터")
    private List<CandidateLocationResponse> candidateLocation;

    @Schema(description = "찜한 장소 리스트 데이터")
    private List<BookmarkResponse> bookmarks;

    public ListScheduleResponse(List<CandidateLocationResponse> candidateLocation, List<BookmarkResponse> bookmarks) {
        this.candidateLocation = candidateLocation;
        this.bookmarks = bookmarks;
    }
}
