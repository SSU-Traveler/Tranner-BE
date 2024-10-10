package com.project.capstone.schedule.dto.response;

import com.project.capstone.schedule.domain.Schedule;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FindScheduleDTO {

    String name;

    Integer howManyPeople;

    LocalDate startDate;

    LocalDate endDate;

    public static FindScheduleDTO of(Schedule schedule){
        return FindScheduleDTO.builder()
                .name(schedule.getName())
                .howManyPeople(schedule.getHowManyPeople())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
    }
}
