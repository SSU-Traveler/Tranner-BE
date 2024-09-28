package com.project.capstone.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.capstone.member.dto.response.CustomUserDetails;
import io.jsonwebtoken.io.IOException;
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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

// 로그인할 때 사용자를 인증하는 필터

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*10L);

        response.addHeader("Authorization", "Bearer " + token);

    }

    // 검증에 실패한 경우
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        response.setStatus(401);
    }
}
