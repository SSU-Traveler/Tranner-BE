package com.project.capstone.global.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.bookmark.repository.BookmarkRepository;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.candidateLocation.repository.CandidateLocationRepository;
import com.project.capstone.global.dto.KakaoDTO;
import com.project.capstone.global.dto.response.LoginResponse;
import com.project.capstone.global.jwt.JwtUtil;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import com.project.capstone.schedule.dto.response.BookmarkResponse;
import com.project.capstone.schedule.dto.response.CandidateLocationResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final CandidateLocationRepository candidateLocationRepository;
    private final BookmarkRepository bookmarkRepository;

    @Value("${KAKAO_CLIENT_ID}")
    private String KAKAO_CLIENT_ID;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${KAKAO_REDIRECT_URL}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    //컨트롤러에서 리턴받은 인증 코드값을 통해 카카오 인증 서버에 카카오 액세스 토큰을 요청합니다.
    public KakaoDTO getKakaoInfo(String code) throws Exception {
        if (code == null) throw new Exception("Failed get authorization code");

        String accessToken = "";
        String refreshToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , "authorization_code");
            params.add("client_id"    , KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code"         , code);
            params.add("redirect_uri" , KAKAO_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken  = (String) jsonObj.get("access_token");
            refreshToken = (String) jsonObj.get("refresh_token");
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생 = {}",e);
            throw new Exception("API call failed");
        }

        log.info("액세스 토큰 = {}",accessToken);

        return getUserInfoWithToken(accessToken);
    }

    // 전달받은 카카오 액세스 토큰을 통해 사용자 정보를 가져온다.
    private KakaoDTO getUserInfoWithToken(String accessToken) throws Exception {
        //HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        log.info("액세스 토큰으로 부터 얻은 사용자 정보 response.getBody() = {}",response.getBody());

        //Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        long id = (long) jsonObj.get("id");
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));

        return KakaoDTO.builder()
                .id(id)
                .email(email)
                .nickname(nickname).build();
    }

    // jwt 토큰을 발급 및 프론트로 전달
    public void makeAndSendJwtToken(KakaoDTO kakaoInfo, HttpServletResponse response) throws IOException {

        // jwt 토큰 발급
        String accessToken = jwtUtil.createAccessToken(Long.toString(kakaoInfo.getId()), "USER");
        String refreshToken = jwtUtil.createRefreshToken(Long.toString(kakaoInfo.getId()), "USER");

        // jwt 토큰 프론트로 전달
        Member member = memberRepository.findByUsername(Long.toString(kakaoInfo.getId()));

        List<CandidateLocation> list = candidateLocationRepository.findByMember(member);
        List<CandidateLocationResponse> candidateLocationList = list.stream().map(CandidateLocationResponse::of).toList();

        List<Bookmark> bookmarks = bookmarkRepository.findByMember(member);
        List<BookmarkResponse> bookmarkResponses = bookmarks.stream().map(BookmarkResponse::of).toList();

        // 응답을 JSON 형식으로 쓰기
        LoginResponse loginResponse = new LoginResponse(candidateLocationList,bookmarkResponses,
                accessToken, refreshToken,
                member.getUsername(),member.getNickname(),
                60 * 60 * 1000,14 * 24 * 60 * 60 * 1000);

        response.setContentType("application/json");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));

        log.info("{}",loginResponse);

        log.info("{} 로그인 성공",Long.toString(kakaoInfo.getId()));
    }
}
