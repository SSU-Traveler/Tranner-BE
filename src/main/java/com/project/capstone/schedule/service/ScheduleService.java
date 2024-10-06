package com.project.capstone.schedule.service;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.schedule.domain.DetailSchedule;
import com.project.capstone.schedule.domain.Schedule;
import com.project.capstone.schedule.dto.request.AddScheduleRequest;
import com.project.capstone.schedule.dto.response.FindScheduleDTO;
import com.project.capstone.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        List<DetailSchedule> detailSchedules = newScheduleDTO.getDetailSchedules().stream()
                .map(dto -> DetailSchedule.builder()
                        .daySequence(dto.getDaySequence())
                        .locationSequence(dto.getLocationSequence())
                        .locationName(dto.getLocationName())
                        .build())
                .collect(Collectors.toList());

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
}
