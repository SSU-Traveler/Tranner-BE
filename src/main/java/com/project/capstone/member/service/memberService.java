package com.project.capstone.member.service;

import com.project.capstone.member.repository.memberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class memberService {
    private final memberRepository memberRepository;
}
