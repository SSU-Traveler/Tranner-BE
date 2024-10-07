package com.project.capstone.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class AddScheduleRequest{

    @NotBlank(message = "여행 이름은 필수값입니다.")
    String name;

    @NotNull(message = "여행 인원수는 필수값입니다.")
    @Range(min = 1, max = 12)
    Integer howManyPeople;

    @NotNull(message = "여행 시작일은 필수값입니다.")
    LocalDate startDate;

    @NotNull(message = "여행 종료일은 필수값입니다.")
    LocalDate endDate;

    @NotNull
    List<DetailScheduleDTO> detailSchedules;
}
