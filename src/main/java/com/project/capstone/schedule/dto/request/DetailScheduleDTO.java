package com.project.capstone.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DetailScheduleDTO {

    @NotBlank
    Integer daySequence;

    @NotBlank
    Integer locationSequence;

    @NotBlank
    String locationName;
}
