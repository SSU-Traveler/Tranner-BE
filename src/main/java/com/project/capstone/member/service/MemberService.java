package com.project.capstone.member.service;

import com.project.capstone.member.domain.Member;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
//import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static java.time.LocalDateTime.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void register(MemberRegisterRequest request) {
        if(memberRepository.existsByUsername(request.username())) {
            return;
        }
        Member member = Member.builder()
                .username(request.username())
                .password(bCryptPasswordEncoder.encode(request.password())) //비밀번호는 암호화하여 저장
                .email(request.username())
                .nickname(request.nickname())
                .registerDate(LocalDate.now())
                .role("ROLE_ADMIN")
                .build();
        memberRepository.save(member);

    }

}
