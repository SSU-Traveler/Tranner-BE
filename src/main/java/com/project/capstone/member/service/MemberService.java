package com.project.capstone.member.service;

import com.project.capstone.global.exception.BusinessLogicException;
import com.project.capstone.global.exception.ExceptionCode;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
import com.project.capstone.member.dto.response.EmailVerificationResult;
import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.bookmark.repository.BookmarkRepository;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.candidateLocation.repository.CandidateLocationRepository;
import com.project.capstone.member.dto.request.MemberEditRequest;
import com.project.capstone.member.dto.response.MemberEditPageResponse;
import com.project.capstone.member.dto.response.MainpageResponse;
import com.project.capstone.member.dto.response.MypageResponse;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.schedule.domain.Schedule;
import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import com.project.capstone.schedule.dto.response.ScheduleResponse;
import com.project.capstone.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ScheduleRepository scheduleRepository;
    private final CandidateLocationRepository candidateLocationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Map<String, String> emailVerificationCodes = new HashMap<>(); // 이메일과 인증 코드 매핑
    private final Map<String, Long> emailVerificationExpirations = new HashMap<>(); // 이메일과 만료 시간 매핑

    private final MailService mailService;

    public void register(MemberRegisterRequest request) {
        //이미 등록된 이메일
        if (memberRepository.existsByEmail(request.memberEmail())) {
            throw new BusinessLogicException(ExceptionCode. MEMBER_EMAIL_EXISTS);
        }
        Member member = Member.builder()
                .username(request.username())
                .password(bCryptPasswordEncoder.encode(request.password())) //비밀번호는 암호화하여 저장
                .email(request.memberEmail())
                .nickname(request.nickname())
                .registerDate(LocalDate.now())
                .role("USER") // ADMIN은 관리자만 주도록 설정해야함
                .build();
        memberRepository.save(member);
    }
    public boolean idDuplicatedCheck(String username) {
        //중복이 있으면 true 중복이 없으면 false
        return memberRepository.existsByUsername(username);
    }

    // 토큰에서 추출한 사용자 정보로 마이페이지에서 조회할 찜 리스트, 스케줄 리스트 반환
    public MypageResponse getMyPage(String username){
        Member member = memberRepository.findByUsername(username);

        List<Bookmark> bookmarks = bookmarkRepository.findAllByMemberId(member.getId()); // 멤버가 찜한 장소 리스트 반환
        List<BookmarkResponse> bookmarksList = bookmarks.stream().map(BookmarkResponse::of).toList();
        log.info("마이페이지 내부 북마크 = {}", bookmarksList);

        List<Schedule> schedules = scheduleRepository.findAllById(member.getId()); // 멤버가 만든 스케줄 반환
        List<ScheduleResponse> schedulesList = schedules.stream().map(ScheduleResponse::of).toList();
        log.info("마이페이지 내부 스케줄 = {}", schedulesList);

        MypageResponse mypageResponse = new MypageResponse(bookmarksList, schedulesList);

        return mypageResponse;
    }

    @Transactional
    public MemberEditPageResponse getMemberEditPage(String username){
        Member member = memberRepository.findByUsername(username);

        MemberEditPageResponse ret=new MemberEditPageResponse(member.getNickname(),member.getEmail());

        return ret;
    }

    @Transactional
    public boolean editMember(String username,
                              MemberEditRequest memberEditRequest) {

        Member member = memberRepository.findByUsername(username);

        member.changeNickname(memberEditRequest.getNickname());

        return true;
    }

    // 사용자의 장바구니 정보를 추출
    public MainpageResponse getCandidateLocations(String username){
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + username);
        }
        List<CandidateLocation> candidateLocations = candidateLocationRepository.findAllByMemberId(member.getId());
        List<CandidateLocationResponse> candidateLocationList = candidateLocations.stream()
                .map(CandidateLocationResponse::of)
                .toList();
        log.info("멤버의 장바구니 정보 = {}", candidateLocationList);
        return new MainpageResponse(candidateLocationList, null);
    }

    // 사용자의 찜 목록 정보를 추출
    public MainpageResponse getBookmarkLocations(String username) {
        // 멤버 정보 가져오기
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + username);
        }
        // 찜 목록 가져오기
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMemberId(member.getId());
        List<BookmarkResponse> bookmarkList = bookmarks.stream()
                .map(BookmarkResponse::of)
                .toList();
        log.info("멤버의 찜 목록 정보 = {}", bookmarkList);

        // MainpageResponse 생성 및 반환
        return new MainpageResponse(null, bookmarkList);
    }


    //회원가입시 이메일에 인증코드 보내기
    public void sendCodeToEmailForRegistration(String email) {

        //알맞은 이메일 형식이 아닌경우
        if (!isValidEmailFormat(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }

        // 이미 등록된 이메일인지 확인
        if (isEmailRegistered(email)) {
            throw new BusinessLogicException(ExceptionCode. MEMBER_EMAIL_EXISTS);
        }
        sendVerificationCode(email);
    }

    //비밀번호 변경시 이메일에 인증코드 보내기
    public void sendCodeToEmailForPasswordReset(String email) {
        if (!isValidEmailFormat(email)) {
            throw new BusinessLogicException(ExceptionCode.EMAIL_NOT_REGISTERED);
        }
        sendVerificationCode(email);
    }


    // 공통으로 인증 코드를 전송하는 메서드
    private void sendVerificationCode(String email) {
        String code = generateRandomCode(); // 랜덤 코드 생성
        log.info("인증코드 :{}", code);
        mailService.sendEmail(email, "Trannere 인증코드", "인증 코드: " + code);

        // 인증 코드와 만료 시간 저장
        emailVerificationCodes.put(email, code);
        emailVerificationExpirations.put(email, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)); // 5분 유효
    }

    //랜덤코드 생성메서드
    private String generateRandomCode() {
        int randomCode = new Random().nextInt(999999); // 0~999999 사이의 난수 생성
        return String.format("%06d", randomCode); // 6자리 문자열로 변환
    }

    //인증코드 확인 메서드
    public EmailVerificationResult verificationCode(String email, String authCode) {
        String storedCode = emailVerificationCodes.get(email);
        Long expirationTime = emailVerificationExpirations.get(email);

        // 인증 코드 유효성 검사
        if (storedCode == null || !storedCode.equals(authCode)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_VERIFICATION_CODE);
        }

        // 만료 시간 확인
        if (System.currentTimeMillis() > expirationTime) {
            throw new BusinessLogicException(ExceptionCode.VERIFICATION_CODE_EXPIRED);
        }
        emailVerificationCodes.remove(email);
        emailVerificationExpirations.remove(email);
        return new EmailVerificationResult("인증이 완료되었습니다.", true);

    }

    public String findUsernameByEmail(String email) {

        Optional<Member> member = memberRepository.findByEmail(email);

        //등록된 사용자가 아닌경우
        if(member.isEmpty()){
            throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
        }
        log.info("user id : {}", member.get().getId());
        return member.get().getUsername();

    }
    public void changePassword(String email, String newPassword) {
        Optional<Member> member = memberRepository.findByEmail(email);
        log.info("member객체:{}",member);
        if (member.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
        }
        // Optional에서 Member 객체 추출
        Member member2 = member.get();

        // 비밀번호 암호화 후 변경
        member2.changePassword(newPassword, bCryptPasswordEncoder);

        // 변경된 비밀번호 저장
        memberRepository.save(member2);
    }
    // 이메일 형식 검증 메서드
    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; // 간단한 이메일 정규식
        return email.matches(emailRegex);
    }

    // 이메일 등록 여부 확인 메서드
    private boolean isEmailRegistered(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

}
