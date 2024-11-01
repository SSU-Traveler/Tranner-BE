package com.project.capstone.member.controller;

import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.dto.request.MemberEditRequest;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
import com.project.capstone.member.dto.response.MemberEditPageResponse;

import com.project.capstone.member.dto.response.EmailVerificationResult;
import com.project.capstone.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import com.project.capstone.member.dto.response.MypageResponse;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody MemberRegisterRequest request) {
        memberService.register(request);
        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }

    //@GetMapping("/logout")
    //public ResponseEntity<String> logout(HttpSession session) {}

    // 마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<MypageResponse> mypage(HttpServletRequest request) {
        String tokenStr = request.getHeader("Authorization"); // jwt토큰에서 사용자 정보 추출
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);
        log.info("마이페이지를 요청한 username은 = {}", username);
        MypageResponse mypageResponse = memberService.getMyPage(username);
        return ResponseEntity.ok().body(mypageResponse);
    }

    @GetMapping
    public ResponseEntity<MemberEditPageResponse> edit(HttpServletRequest request) {
        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        log.info("마이페이지에서 프로필 수정 페이지를 요청한 username은 = {}", username);

        MemberEditPageResponse memberEditPageResponse = memberService.getMemberEditPage(username);

        return ResponseEntity.ok().body(memberEditPageResponse);
    }

    @PatchMapping
    public ResponseEntity<Void> edit(HttpServletRequest request,
                                     @Validated @RequestBody MemberEditRequest memberEditRequest) {

        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        log.info("마이페이지에서 프로플 수정을 요청한 username은 = {}", username);

        memberService.editMember(username, memberEditRequest);


        return ResponseEntity.status(HttpStatus.FOUND) // 302 리다이렉션
                .location(URI.create("/member")) // 리다이렉트할 URL
                .build();
    }

    //이메일 보내기(프론트에서 회원가입, 아이디 찾기시 이 url사용하기)
    @PostMapping("/emails/verification-requests")
    public ResponseEntity<Void> sendMessage(@RequestBody Map<String,String> request) {
        memberService.sendCodeToEmail(request.get("email"));
        return ResponseEntity.ok().build();
    }

    //이메일 인증코드 확인
    @GetMapping("/emails/verifications")
    public ResponseEntity<EmailVerificationResult> verificationEmail( @RequestBody Map<String, String> request) {

        String email = request.get("email");
        log.info("세션email:{}", email);

        String authCode = request.get("authCode");
        log.info("사용자가 보낸 인증코드 :{}",authCode);

        EmailVerificationResult response = memberService.verificationCode(email, authCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //아이디 찾기
    @PostMapping("/findid")
    public ResponseEntity<Map<String, String>> findid(@Valid @RequestBody Map<String, String> request) {

        String email = request.get("email");
        log.info("이메일 요청: {} ", email);

        String username = memberService.findUsernameByEmail(email);
        log.info("이메일 요청한 사용자: {} ", username);


        Map<String, String> response = new HashMap<>();
        response.put("username", username);

        return ResponseEntity.ok(response);
    }

    //비밀번호 찾기(이메일 인증 후 비밀번호 찾기 가능)
    @PostMapping("/findpw/emails")
    public ResponseEntity<String> sendResetPasswordEmail(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        memberService.sendCodeToEmail(email);
        session.setAttribute("email", email);
        log.info("세션email:{}", session.getAttribute("email"));
        return ResponseEntity.ok("비밀번호 재설정 이메일 인증코드를 보냈습니다.");
    }

    //이메일 인증코드 확인 후 비밀번호 변경페이지로 리다이렉트
    @PostMapping("/findpw/verify")
    public ResponseEntity<String> verifyCodeForPasswordRest(
            @RequestBody Map<String, String> request, HttpSession session) {

        String email = (String) session.getAttribute("email");
        log.info("세션email2:{}", email);
        String authCode = request.get("authCode");

        EmailVerificationResult result = memberService.verificationCode(email, authCode);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }

    //비밀번호 변경
    @PostMapping("findpw/change")
    public ResponseEntity<String> changePassword(
            @RequestBody Map<String, String> request, HttpSession session) {
        String newPassword = request.get("newPassword");
        String email = (String) session.getAttribute("email");

       //프론트에서 비밀번호 조건 추가

        memberService.changePassword(email, newPassword);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }


}
