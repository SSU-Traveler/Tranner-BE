package com.project.capstone.schedule.dto.response;

import com.project.capstone.candidateLocation.domain.CandidateLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CandidateLocationResponse(
        @NotBlank
        @Schema(description = "장바구니 장소")
        String location
) {
    public static CandidateLocationResponse of(CandidateLocation candidateLocation) {
        return CandidateLocationResponse.builder()
                .location(candidateLocation.getLocation())
                .build();
    }
}
