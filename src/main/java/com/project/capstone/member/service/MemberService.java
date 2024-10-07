package com.project.capstone.member.service;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.bookmark.repository.BookmarkRepository;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
//import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.member.dto.response.MypageResponse;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.schedule.domain.Schedule;
import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.ScheduleResponse;
import com.project.capstone.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDateTime.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ScheduleRepository scheduleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void register(MemberRegisterRequest request) {
        if(memberRepository.existsByUsername(request.username())) {
            return;
        }
        Member member = Member.builder()
                .username(request.username())
                .password(bCryptPasswordEncoder.encode(request.password())) //비밀번호는 암호화하여 저장
                .email(request.memberEmail())
                .nickname(request.nickname())
                .registerDate(LocalDate.now())
                .role("ROLE_ADMIN")
                .build();
        memberRepository.save(member);
    }

    public MypageResponse getMyPage(String username){
        Member member = memberRepository.findByUsername(username);

        List<Bookmark> bookmarks = bookmarkRepository.findAllById(member.getId()); // 멤버가 찜한 장소 리스트 반환
        List<BookmarkResponse> bookmarksList = bookmarks.stream().map(BookmarkResponse::of).toList();
        log.info("마이페이지 내부 북마크 = {}", bookmarksList);

        List<Schedule> schedules = scheduleRepository.findAllById(member.getId()); // 멤버가 만든 스케줄 반환
        List<ScheduleResponse> schedulesList = schedules.stream().map(ScheduleResponse::of).toList();
        log.info("마이페이지 내부 스케줄 = {}", schedulesList);

        MypageResponse mypageResponse = new MypageResponse(bookmarksList, schedulesList);

        return mypageResponse;
    }

}
