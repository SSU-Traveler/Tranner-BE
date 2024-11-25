package com.project.capstone.member.dto.request;

import com.project.capstone.candidateLocation.domain.CandidateLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CandidateLocationRequest(
    @NotBlank
    @Schema(description = "장바구니 장소")
    String location
){
    public static CandidateLocationRequest of (CandidateLocation candidateLocation){
        return CandidateLocationRequest.builder()
                .location(candidateLocation.getLocation())
                .build();
    }
}