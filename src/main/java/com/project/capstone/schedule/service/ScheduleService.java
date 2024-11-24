package com.project.capstone.schedule.service;

import com.project.capstone.global.exception.BusinessLogicException;
import com.project.capstone.global.exception.ExceptionCode;
import com.project.capstone.global.exception.ScheduleNotFoundException;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.schedule.domain.DetailSchedule;
import com.project.capstone.schedule.domain.Schedule;
import com.project.capstone.schedule.dto.request.AddScheduleRequest;
import com.project.capstone.schedule.dto.request.DetailScheduleDTO;
import com.project.capstone.schedule.dto.request.EditScheduleRequest;
import com.project.capstone.schedule.dto.response.FindScheduleDTO;
import com.project.capstone.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.bookmark.repository.BookmarkRepository;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.candidateLocation.repository.CandidateLocationRepository;
import com.project.capstone.schedule.dto.request.GetAddSchedule;
import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import com.project.capstone.schedule.dto.response.ListScheduleResponse;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final CandidateLocationRepository locationRepository;
    private final BookmarkRepository bookmarkRepository;

    // 해당 멤버에 스케줄을 저장 (일자별 스케줄도 저장됨)
    @Transactional
    public void saveSchedule(String username, AddScheduleRequest newScheduleDTO) {

        Member member = memberRepository.findByUsername(username);

        List<DetailSchedule> detailSchedules = getDetailSchedulesFromDto(newScheduleDTO.getDetailSchedules());

        Schedule schedule = Schedule.builder()
                .name(newScheduleDTO.getName())
                .howManyPeople(newScheduleDTO.getHowManyPeople())
                .startDate(newScheduleDTO.getStartDate())
                .endDate(newScheduleDTO.getEndDate())
                .detailSchedules(detailSchedules)
                .build();

        member.addSchedule(schedule);
    }

    // 해당 멤버의 모든 스케줄을 리턴함
    @Transactional
    public List<FindScheduleDTO> findAllSchedules(String username){
        Member member = memberRepository.findByUsername(username);

        List<FindScheduleDTO> foundSchedules = scheduleRepository.findAllByMember_Username(username).stream().map(FindScheduleDTO::of).toList();

        return foundSchedules;
    }

    // 해당 멤버의 스케줄 中 하나를 수정 (일자별 스케줄도 수정됨)
    @Transactional
    public void editSchedule(String username,
                             Long scheduleId,
                             EditScheduleRequest editScheduleDTO){

        // 스케줄을 수정할 멤버
        Member member = memberRepository.findByUsername(username);

        // 기존 스케줄 삭제
        try{
            Schedule beforeSchedule = scheduleRepository.findById(scheduleId).get();
            beforeSchedule.deleteThisSchedule();
            log.info("beforeSchedule = {}",beforeSchedule);

//            scheduleRepository.deleteById(scheduleId);
            member.deleteSchedule(beforeSchedule);
        }
        catch (EmptyResultDataAccessException e){
            throw new ScheduleNotFoundException(e);
        }

        // 바꿀 스케줄 추가
        Schedule afterSchedule = getScheduleFromDto(editScheduleDTO, getDetailSchedulesFromDto(editScheduleDTO.getDetailSchedules()));
        member.addSchedule(afterSchedule);
    }

    // Dto EditScheduleRequest를 Schedule로 변환
    private static Schedule getScheduleFromDto(EditScheduleRequest editScheduleDTO, List<DetailSchedule> detailSchedules) {
        return Schedule.builder()
                .name(editScheduleDTO.getName())
                .howManyPeople(editScheduleDTO.getHowManyPeople())
                .startDate(editScheduleDTO.getStartDate())
                .endDate(editScheduleDTO.getEndDate())
                .detailSchedules(detailSchedules)
                .build();
    }

    // Dto List<DetailScheduleDTO>를 List<DetailSchedule>로 변환
    private static List<DetailSchedule> getDetailSchedulesFromDto(List<DetailScheduleDTO> editScheduleDTO) {
        return editScheduleDTO.stream()
                .map(dto -> DetailSchedule.builder()
                        .daySequence(dto.getDaySequence())
                        .locationSequence(dto.getLocationSequence())
                        .locationName(dto.getLocationName())
                        .build())
                .collect(Collectors.toList());
    }

    // 여행 계획 생성 페이지 첫 부분 들어왔을 때
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

    // 스케줄 하나를 삭제
    @Transactional
    public void deleteSchedule(String username, Long scheduleId) {

        Member member = memberRepository.findByUsername(username);

        // 스케줄 존재 여부 확인
        Optional<Schedule> schedule4Delete = scheduleRepository.findById(scheduleId);

        if(schedule4Delete.isEmpty()) throw new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND);
        else member.deleteSchedule(schedule4Delete.get());
    }
}