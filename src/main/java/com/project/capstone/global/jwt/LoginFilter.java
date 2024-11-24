package com.project.capstone.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.candidateLocation.repository.CandidateLocationRepository;
import com.project.capstone.global.dto.response.LoginResponse;
import com.project.capstone.global.exception.ExceptionCode;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.dto.response.CustomUserDetails;
import com.project.capstone.member.dto.response.MainpageResponse;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.member.service.MemberService;
import com.project.capstone.redis.RedisSessionService;
import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// 로그인할 때 사용자를 인증하는 필터

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CandidateLocationRepository locationRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    // RedisSessionService 추가
    private final RedisSessionService redisSessionService;




    //사용자 로그인 데이터 추출
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> loginData;

        try {
            // 요청 바디에서 데이터를 추출
            loginData = objectMapper.readValue(request.getInputStream(), Map.class);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read login data", e);
        }

        // 사용자가 입력한 정보에서 아이디와 비밀번호를 추출함
        String username = loginData.get("username");
        String password = loginData.get("password");

        // 인증을 수행하는 AuthenticationManager로 정보를 보내기 위해선 Token에 정보를 담아야함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken); // 검증을 진행하기 위해 Token을 보냄
    }

    // 검증에 성공했을 경우
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        Member member = memberRepository.findNicknameByUsername(username);

        String nickname = member.getNickname();
        //Access Token 생성
        String accessToken = jwtUtil.createAccessToken(username, role);
        //Refresh Token 생성
        String refreshToken = jwtUtil.createRefreshToken(username, role);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh-Token", "Bearer " + refreshToken);

        // Redis에 로그인 세션 정보 저장
        String sessionId = "session:" + username; // 세션 ID 생성
        redisSessionService.saveSession(sessionId, customUserDetails);


        // Redis에 사용자 아이디 저장
        String redisUserKey = "user:" + username; // 사용자 ID 저장을 위한 Key
        redisSessionService.saveSession(redisUserKey, username);

        // 장바구니 정보 가져오기
        MainpageResponse candidateLocationResponse = memberService.getCandidateLocations(username);
        List<CandidateLocationResponse> candidateLocation = candidateLocationResponse.getCandidateLocation();

        // 찜 목록 정보 가져오기
        MainpageResponse bookmarkResponse = memberService.getBookmarkLocations(username);
        List<BookmarkResponse> bookmark = bookmarkResponse.getBookmark();

        List<CandidateLocation> list = locationRepository.findByMember(member);
        List<CandidateLocationResponse> candidateLocationList = list.stream().map(CandidateLocationResponse::of).toList();

        // 응답을 JSON 형식으로 쓰기
        LoginResponse loginResponse = new LoginResponse(candidateLocation, bookmark, accessToken, refreshToken, username, nickname , 60 * 60 * 1000,14 * 24 * 60 * 60 * 1000);
        response.setContentType("application/json");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));

    }

    // 검증에 실패한 경우
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        ExceptionCode exceptionCode = ExceptionCode.FAILED_AUTHENTICATION;
        // 응답 상태 코드와 메시지 설정
        response.setStatus(exceptionCode.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // JSON 형식으로 오류 메시지 작성
            String jsonResponse = new ObjectMapper().writeValueAsString(
                    Map.of(
                            "status", exceptionCode.getStatus(),
                            "message", exceptionCode.getMessage()
                    )
            );

            // 응답 본문에 JSON 메시지 쓰기
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}