package com.project.capstone.schedule.dto.response;

import com.project.capstone.schedule.domain.DetailSchedule;
import com.project.capstone.schedule.domain.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ScheduleResponse(

        @NotBlank
        @Schema(description = "첫번째 날의 첫번째 장소의 locationName")
        String placeId,

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
                .placeId(getPlaceId(schedule))
                .id(schedule.getId())
                .name(schedule.getName())
                .howManyPeople(schedule.getHowManyPeople())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
    }

    // 해당 schedule의 detailSchedules 中 첫번째 detailSchedule의 locationName을 리턴
    private static String getPlaceId(Schedule schedule){

        List<DetailSchedule> detailSchedules = schedule.getDetailSchedules();

        for (DetailSchedule detailSchedule : detailSchedules) {
            if(detailSchedule.getDaySequence()==1 && detailSchedule.getLocationSequence()==1){
                return detailSchedule.getLocationName();
            }
        }

        return null;
    }
}
