package com.project.capstone.member.service;

import com.project.capstone.member.domain.Member;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
import com.project.capstone.member.dto.response.MemberResponse;
import com.project.capstone.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse registerMember(MemberRegisterRequest request) {


        if (memberRepository.existsById(request.id())) {
            return new MemberResponse(false, "아이디가 이미 존재합니다.", null);
        }

        if (memberRepository.existsByNickname(request.nickname())) {
            return new MemberResponse(false, "닉네임이 이미 사용 중입니다.", null);
        }

        if (memberRepository.existsByEmail(request.memberEmail())) {
            return new MemberResponse(false, "이메일이 이미 등록되어 있습니다.", null);
        }
        Member member = new Member();
        member.setId(request.id());
        member.setPw(request.pw());
        member.setEmail(request.memberEmail());
        member.setNickname(request.nickname());
        member.setRegisterdate(LocalDate.now());
        memberRepository.save(member);

        return new MemberResponse(true, "회원가입이 완료되었습니다.", member.getMemberid());
    }
}
