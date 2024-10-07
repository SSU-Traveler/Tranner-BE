package com.project.capstone.schedule.service;

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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

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
}
