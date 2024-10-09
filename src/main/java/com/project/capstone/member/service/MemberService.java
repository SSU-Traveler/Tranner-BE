package com.project.capstone.member.service;

import com.project.capstone.global.exception.BusinessLogicException;
import com.project.capstone.global.exception.ExceptionCode;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
import com.project.capstone.member.dto.response.EmailVerificationResult;
import com.project.capstone.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;



@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Map<String, String> emailVerificationCodes = new HashMap<>(); // 이메일과 인증 코드 매핑
    private final Map<String, Long> emailVerificationExpirations = new HashMap<>(); // 이메일과 만료 시간 매핑

    private final MailService mailService;

    public void register(MemberRegisterRequest request) {
        if (memberRepository.existsByUsername(request.username())) {
            throw new BusinessLogicException(ExceptionCode.USERID_EXISTS);
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

    public void sendCodeToEmail(String email) {
        String code = generateRandomCode(); // 랜덤 코드 생성
        log.info("인증코드 :{}",code);
        mailService.sendEmail(email, "Trannere 인증코드", "인증 코드: " + code);

        // 인증 코드와 만료 시간 저장
        emailVerificationCodes.put(email, code);
        emailVerificationExpirations.put(email, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)); // 5분 유효
    }

    private String generateRandomCode() {
        int randomCode = new Random().nextInt(999999); // 0~999999 사이의 난수 생성
        return String.format("%06d", randomCode); // 6자리 문자열로 변환
    }

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
        log.info("반환된 멤버 = {}", member);
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

}
