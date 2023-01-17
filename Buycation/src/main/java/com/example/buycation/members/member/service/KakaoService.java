package com.example.buycation.members.member.service;

import com.example.buycation.members.member.dto.KakaoMemberInfoDto;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.member.mapper.MemberMapper;
import com.example.buycation.members.member.repository.MemberRepository;
import com.example.buycation.security.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final MemberMapper memberMapper;


    @Transactional
    public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoMemberInfoDto kakaoMemberInfoDto = getKakaoMemberInfo(accessToken);

        // 3. 필요시에 회원가입
        Member kakaoMember = registerKakaoMemberIfNeeded(kakaoMemberInfoDto);

        // 4. JWT 토큰 반환
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(kakaoMember.getEmail()));
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "00a53b1769c1cc0a1142657ec7e2b793");
        body.add("redirect_uri", "http://localhost:3000/api/kakaomember");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoMemberInfoDto(id, nickname, email);
    }

    // 3. 필요시에 회원가입
    private Member registerKakaoMemberIfNeeded(KakaoMemberInfoDto kakaoMemberInfoDto) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoMemberInfoDto.getId();
        Member kakaoMember = memberRepository.findByKakaoId(kakaoId).orElse(null);
        if (kakaoMember == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoMemberInfoDto.getEmail();
            Member sameEmailUser = memberRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoMember = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoMember = kakaoMember.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoMemberInfoDto.getEmail();

                String nickname = kakaoMemberInfoDto.getNickname();
                //닉네임 유효성 체크 및 중복체크
                if (!Pattern.matches("^(?=.*[a-z0*9가-힣])[a-z0-9가-힣]{2,10}$", nickname)) {
                    nickname = "건전한닉네임";
                }
                while (memberRepository.findByNickname(nickname).isPresent()) {
                        //마지막 문자가 숫자면 + 1
                        String substring = nickname.substring(nickname.length() - 1);
                        if (Pattern.matches("^(?=.*[0-9])[0-9]", substring)) {
                            nickname = nickname.substring(0, nickname.length() - 1) + (Integer.parseInt(substring) + 1);
                        } else nickname = nickname + 1;
                }
                kakaoMember = memberMapper.toMember(email, kakaoId, encodedPassword, nickname);
            }
            memberRepository.save(kakaoMember);
        }
        return kakaoMember;
    }
}
