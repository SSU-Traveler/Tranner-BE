package com.project.capstone.schedule.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FindScheduleDTO {

    String name;

    Integer howManyPeople;

    LocalDate startDate;

    LocalDate endDate;
    
}
