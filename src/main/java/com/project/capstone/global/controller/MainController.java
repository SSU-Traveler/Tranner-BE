package com.project.capstone.global.controller;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.bookmark.repository.BookmarkRepository;
import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.dto.response.MainpageResponse;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.member.service.MemberService;
import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    // 메인페이지
    // 클라이언트는 jwt 토큰을 항상 포함해서 보내기 때문에 jwtfilter에서 검사하지 않아도 추출할 수 있음
    @GetMapping("/main")
    public ResponseEntity<?> main(HttpServletRequest request) {
        String tokenStr = request.getHeader("Authorization"); // jwt토큰에서 사용자 정보 추출
        if(tokenStr == null) {
            return ResponseEntity.ok().body("로그인하지 않은 사용자");
        }
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);
        log.info("메인페이지를 요청한 username은 = {}", username);

        // 장바구니 정보 가져오기
        MainpageResponse candidateLocationResponse = memberService.getCandidateLocations(username);
        List<CandidateLocationResponse> candidateLocations = candidateLocationResponse.getCandidateLocation();

        // 찜 목록 정보 가져오기
        MainpageResponse bookmarkResponse = memberService.getBookmarkLocations(username);
        List<BookmarkResponse> bookmarks = bookmarkResponse.getBookmark();

        // MainpageResponse 객체 생성 및 반환
        MainpageResponse mainpageResponse = new MainpageResponse(candidateLocations, bookmarks);
        log.info("최종 메인페이지 반환 = {}", mainpageResponse);

        return ResponseEntity.ok().body(mainpageResponse);
    }
}
