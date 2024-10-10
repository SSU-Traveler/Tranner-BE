package com.project.capstone.schedule.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;


@Getter
public class GetAddSchedule {
        @Schema(description = "장바구니 리스트")
        List<Location> candidateLocation;
}