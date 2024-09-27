package com.project.capstone.member.repository;

import com.project.capstone.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
//public class MemberRepositoryImpl implements MemberRepository {

//    private final AtomicLong memberIdxGenerator = new AtomicLong(0);
//    private final Map<Long, Member> memberMap = new HashMap<>();
//
//    // 저장
//    public Member save(Member member) {
//        if (member.getMemberId() == null) {
//            Long id = memberIdxGenerator.incrementAndGet();
//            member.setId(id);
//            memberMap.put(id, member);
//            return member;
//        } else {
//            memberMap.replace(member.getId(), member);
//            return member;
//        }
//    }

//}
