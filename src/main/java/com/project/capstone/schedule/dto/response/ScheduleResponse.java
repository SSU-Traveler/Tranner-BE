package com.project.capstone.schedule.dto.response;

import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.schedule.domain.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ScheduleResponse(
        @NotBlank
        @Schema(description = "스케줄 pk")
        Long id,

        @NotBlank
        @Schema(description = "스케줄 이름")
        String name,

        @NotBlank
        @Schema(description = "여행 인원")
        Integer howManyPeople,

        @NotBlank
        @Schema(description = "여행 시작일자")
        LocalDate startDate,

        @NotBlank
        @Schema(description = "여행 종료일자")
        LocalDate endDate
) {
    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .name(schedule.getName())
                .howManyPeople(schedule.getHowManyPeople())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
    }
}
