package com.project.capstone.global.config;

import com.project.capstone.bookmark.repository.BookmarkRepository;
import com.project.capstone.candidateLocation.repository.CandidateLocationRepository;
import com.project.capstone.global.jwt.JwtFilter;
import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.global.jwt.LoginFilter;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.member.service.MemberService;
import com.project.capstone.redis.RedisSessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CandidateLocationRepository candidateLocationRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RedisSessionService redisSessionService;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil, CandidateLocationRepository candidateLocationRepository, @Lazy MemberService memberService , MemberRepository memberRepository, RedisSessionService redisSessionService  ) {


        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.candidateLocationRepository = candidateLocationRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.redisSessionService = redisSessionService;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, BookmarkRepository bookmarkRepository, MemberRepository memberRepository) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());
        http
                .cors((cors) -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://www.tranner.com",  "localhost:5173")); // 허용할 출처
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
                    config.setAllowedHeaders(List.of("*")); // 허용할 헤더
                    config.setAllowCredentials(true); // 자격 증명 허용
                    return config;
                }));

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/member/register","/member/emails/verification-requests", "/member/emails/verifications","/member/findid"
                        ,"/member/findpw/emails","/member/findpw/verify","/member/findpw/change", "/main" , "/member/idDuplicatedCheck","/oauth/callback/kakao","/api/kakaoLogin","/kakaoLogin, /auth/refresh").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
        RedisSessionService RedisSessionService;
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, candidateLocationRepository, memberService ,memberRepository,redisSessionService), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}