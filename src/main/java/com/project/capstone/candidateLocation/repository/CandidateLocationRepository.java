package com.project.capstone.candidateLocation.repository;

import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.member.domain.Member;
import com.project.capstone.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateLocationRepository extends JpaRepository<CandidateLocation, Long> {
    List<CandidateLocation> findAllByMemberId(Long member_id);
    List<CandidateLocation> findByMember(Member member);
}
