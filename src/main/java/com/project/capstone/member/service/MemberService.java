package com.project.capstone.member.service;

import com.project.capstone.member.dto.request.MemberRegisterRequest;
import com.project.capstone.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void registerMember(MemberRegisterRequest request) {

    }
}
