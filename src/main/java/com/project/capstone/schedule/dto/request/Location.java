package com.project.capstone.schedule.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record Location(
        @Schema(description = "위치")
        String location
) {
}