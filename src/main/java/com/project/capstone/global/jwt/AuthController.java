package com.project.capstone.global.jwt;

import com.project.capstone.global.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    //사용자가 새로운 페이지 들어갈때 엑세스 토큰 만료시 프론트에서 이 api요청해서 엑세스 토큰
//    @PostMapping("/refresh")
//    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
//        try {
//            // Refresh Token 검증
//            if (jwtUtil.isExpired(refreshToken)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(Map.of("error", "Refresh Token is expired"));
//            }
//
//            String username = jwtUtil.getUsername(refreshToken);
//            String role = jwtUtil.getRole(refreshToken);
//
//            // 새로운 Access Token 생성
//            String newAccessToken = jwtUtil.createAccessToken(username, role);
//
//            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "Invalid Refresh Token"));
//        }
//    }

    //사용자가 새로운 페이지 들어갈때 엑세스 토큰 만료시 프론트에서 이 api요청해서 엑세스 토큰
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(HttpServletRequest request) {
        // 쿠키에서 Refresh Token 가져오기
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // Refresh Token이 없거나 만료된 경우 처리
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh Token not found"));
        }

        try {
            // Refresh Token 검증
            if (jwtUtil.isExpired(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Refresh Token is expired"));
            }

            String username = jwtUtil.getUsername(refreshToken);
            String role = jwtUtil.getRole(refreshToken);

            // 새로운 Access Token 생성
            String newAccessToken = jwtUtil.createAccessToken(username, role);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid Refresh Token"));
        }
    }
}
