package com.project.capstone.candidateLocation.repository;

import com.project.capstone.candidateLocation.domain.CandidateLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateLocationRepository extends JpaRepository<CandidateLocation, Long> {
}
