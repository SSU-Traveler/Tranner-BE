package com.project.capstone.schedule.dto.response;

import com.project.capstone.bookmark.domain.Bookmark;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BookmarkResponse(
        @NotBlank
        @Schema(description = "찜한 장소")
        String location
) {
    public static BookmarkResponse of(Bookmark bookmark) {
        return BookmarkResponse.builder()
                .location(bookmark.getLocation())
                .build();
    }
}
