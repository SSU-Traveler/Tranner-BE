package com.project.capstone.member.dto.response;

import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.ListScheduleResponse;
import com.project.capstone.schedule.dto.response.ScheduleResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MypageResponse <T>{
    @Schema(description = "스케줄 리스트 데이터")
    private List<ScheduleResponse> schedules;

    @Schema(description = "찜한 장소 리스트 데이터")
    private List<BookmarkResponse> bookmarks;

    public MypageResponse(List<ScheduleResponse> schedules, List<BookmarkResponse> bookmarks) {
        this.schedules = schedules;
        this.bookmarks = bookmarks;
    }
}
