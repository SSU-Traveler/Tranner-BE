package com.project.capstone.member.dto.request;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.schedule.dto.response.BookmarkResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.awt.print.Book;

@Builder
public record BookmarkRequest(
        @NotBlank
        @Schema(description = "찜한 장소")
        String location
){
    public static BookmarkRequest of(Bookmark bookmark) {
        return BookmarkRequest.builder()
                .location(bookmark.getLocation())
                .build();
    }
}
