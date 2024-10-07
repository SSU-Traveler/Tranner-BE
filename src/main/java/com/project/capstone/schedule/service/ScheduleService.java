package com.project.capstone.schedule.service;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.bookmark.repository.BookmarkRepository;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.candidateLocation.repository.CandidateLocationRepository;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.schedule.dto.request.GetAddSchedule;
import com.project.capstone.schedule.dto.request.Location;
import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import com.project.capstone.schedule.dto.response.ListScheduleResponse;
import com.project.capstone.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final MemberRepository memberRepository;
    private final CandidateLocationRepository locationRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public ListScheduleResponse AddCandidateLocation(GetAddSchedule request, String username) {
        Member member = memberRepository.findByUsername(username); // 여행 계획을 짤 멤버 정보
        log.info("스케줄을 생성하는 멤버 = {}", member.toString());

        List<CandidateLocation> candidateLocations = request.getCandidateLocation().stream() // 장바구니에 담아놓은 여행지 리스트
                .map(dto -> CandidateLocation.builder()
                        .member(member)
                        .location(dto.location())
                        .build()).collect(Collectors.toList());
        List<CandidateLocationResponse> candidateLocationList = candidateLocations.stream().map(CandidateLocationResponse::of).toList();
        locationRepository.deleteAll(); // 여행 계획을 짜게 되면 이미 완성된 계획이므로 장바구니에 아무것도 있으면 안됨
        // 장바구니는 담아놓고 로그아웃 했을 경우에만 저장 후 로그아웃
        // 로그인했을 경우에 저장된 장바구니 리스트 반환

        List<Bookmark> bookmarks = bookmarkRepository.findAllById(member.getId()); // 멤버가 찜한 장소 리스트 반환
        List<BookmarkResponse> bookmarksList = bookmarks.stream().map(BookmarkResponse::of).toList();
        ListScheduleResponse listScheduleResponse = new ListScheduleResponse(candidateLocationList, bookmarksList); // 장바구니 리스트, 찜한 장소 리스트를 넘겨줌
        return listScheduleResponse;
    }
}
