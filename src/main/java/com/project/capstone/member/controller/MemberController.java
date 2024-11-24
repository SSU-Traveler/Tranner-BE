package com.project.capstone.member.controller;

import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.dto.request.MemberEditRequest;
import com.project.capstone.member.dto.request.MemberRegisterRequest;
import com.project.capstone.member.dto.request.SaveUserInfoRequest;
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

    @GetMapping("/idDuplicatedCheck")
    public ResponseEntity<Boolean> idDuplicatedCheck(@RequestParam String id){
        boolean result =  memberService.idDuplicatedCheck(id);
        return ResponseEntity.ok().body(result);
    }
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

    //회원가입 이메일 보내기
    @PostMapping("/emails/register-requests")
    public ResponseEntity<Void> sendMessageForRegister(@RequestBody Map<String,String> request) {
  
        memberService.sendCodeToEmailForRegistration(request.get("email"));
        return ResponseEntity.ok().build();
    }

    //비밀번호 변경시 이메일 보내기
    @PostMapping("/emails/resetPassword-requests")
    public ResponseEntity<Void> sendMessageForPassword(@RequestBody Map<String,String> request) {
        memberService.sendCodeToEmailForPasswordReset(request.get("email"));
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
        memberService.sendCodeToEmailForPasswordReset(email);
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
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request,
                                                 HttpSession session) {

        String newPassword = request.get("newPassword");
        String email = (String) session.getAttribute("email");

        memberService.changePassword(email, newPassword);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // user가 창을 닫거나, 로그아웃 했을 때, 북마크(찜), 장바구니 리스트를 저장해야됨
    // 왜냐하면 user 가 사용 중간에 내용을 변경했을 테니까
    @PostMapping("/saveUserInfo")
    public ResponseEntity<String> saveUserInfo(HttpServletRequest request,
                                               @RequestBody SaveUserInfoRequest saveUserInfoRequest){

        String tokenStr = request.getHeader("Authorization");
        String token = tokenStr.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        log.info("창을 닫거나, 로그아웃을 한 username은 = {}", username);

        // DTO에서 받은 데이터 확인 (로깅 및 디버깅용)
        log.info("Candidate Locations: " + saveUserInfoRequest.getCandidateLocations());
        log.info("Bookmarks: " + saveUserInfoRequest.getBookmarks());

        // 저장 로직 수행
        memberService.saveUserData(username,saveUserInfoRequest);

        return ResponseEntity.ok("User 정보 (bookmarks(찜 리스트),candidateLocations(장바구니 리스트)) 저장 성공");
    }
}