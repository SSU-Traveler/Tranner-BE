package com.project.capstone.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.candidateLocation.repository.CandidateLocationRepository;
import com.project.capstone.global.dto.response.LoginResponse;
import com.project.capstone.global.exception.BusinessLogicException;
import com.project.capstone.global.exception.ExceptionCode;
import com.project.capstone.member.dto.response.CustomUserDetails;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CandidateLocationRepository locationRepository;


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

        System.out.println(username);
        System.out.println(password);
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

        String token = jwtUtil.createJwt(username, role, 60 * 60 * 24 * 365 * 100L);

        response.addHeader("Authorization", "Bearer " + token);
        List<CandidateLocation> list = locationRepository.findByUsername(username);
        List<CandidateLocationResponse> candidateLocationList = list.stream().map(CandidateLocationResponse::of).toList();
        LoginResponse loginResponse = new LoginResponse(candidateLocationList);

        // 응답을 JSON 형식으로 쓰기
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));

        log.info("{} 로그인 성공",username);
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