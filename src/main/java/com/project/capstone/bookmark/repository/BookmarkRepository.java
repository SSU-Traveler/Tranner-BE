package com.project.capstone.bookmark.repository;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByMemberId(Long member_id);
    List<Bookmark> findByMember(Member member);
}
