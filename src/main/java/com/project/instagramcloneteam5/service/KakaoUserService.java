//package com.project.instagramcloneteam5.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.instagramcloneteam5.config.UserDetailsImpl;
//import com.project.instagramcloneteam5.config.jwt.JwtFilter;
//import com.project.instagramcloneteam5.config.jwt.TokenProvider;
//import com.project.instagramcloneteam5.dto.auth.KakaoUserInfoDto;
//import com.project.instagramcloneteam5.model.Member;
//import com.project.instagramcloneteam5.repository.MemberRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.UUID;
//
//
//@Service
//public class KakaoUserService {
//
//    private final PasswordEncoder passwordEncoder;
//    private final MemberRepository memberRepository;
//
//    private final TokenProvider tokenProvider;
//
//    @Autowired
//    public KakaoUserService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
//        this.memberRepository = memberRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.tokenProvider = tokenProvider;
//    }
//
//
//
//    public HttpHeaders kakaoLogin(String authorityCode) throws JsonProcessingException {
//        // 1. "?????? ??????"??? "????????? ??????" ??????
//        String accessToken = getAccessToken(authorityCode);
//
//        // 2. ???????????? ????????? API ??????
//        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
//
//        // 3. ???????????? ????????????
//        Member kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
//
//        // 4. ?????? ????????? ?????? ??? JWT ?????? return
//        return forceLogin(kakaoUser);
//    }
//
//
//    private String getAccessToken(String code) throws JsonProcessingException {
//        // HTTP Header ??????
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP Body ??????
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", "8e944bb01ee403f06a696f5d81f3be1f");
//        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
//        body.add("code", code);
//
//        // HTTP ?????? ?????????
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
//                new HttpEntity<>(body, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        // HTTP ?????? (JSON) -> ????????? ?????? ??????
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        return jsonNode.get("access_token").asText();
//    }
//
//    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
//        // HTTP Header ??????
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP ?????? ?????????
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        Long id = jsonNode.get("id").asLong();
//        String nickname = jsonNode.get("properties")
//                .get("nickname").asText();
//        String email = jsonNode.get("kakao_account")
//                .get("email").asText();
//
//        System.out.println("????????? ????????? ?????? : " + id + ", " + nickname + ", " + email);
//
//        return new KakaoUserInfoDto(id, nickname, email);
//    }
//
//    private Member registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
//        // DB ??? ????????? Kakao Id ??? ????????? ??????
//        Long kakaoId = kakaoUserInfo.getId();
//        Member kakaoUser = memberRepository.findByKakaoId(kakaoId).orElse(null);
//
//        // ????????? DB??? ????????? ??????
//        if (kakaoUser == null) {
//            //DB ?????? ??? email ??????
//            Member memberWithKakaoEmail = memberRepository.findByKakaoEmail(kakaoUserInfo.getKakaoEmail()).orElse(null);
//            if (memberWithKakaoEmail == null) {
//                //1.????????? ???????????? ??????
//                Member newMember = getNewMemberDataByConvertingKakaoUserToMember(kakaoUserInfo);
//                memberRepository.save(newMember);
//                return newMember;
//            }else {
//                //2.????????? ????????? id ??????
//                memberWithKakaoEmail.setKakaoId(kakaoUserInfo.getId());
//                memberRepository.save(memberWithKakaoEmail);
//                return memberWithKakaoEmail;
//            }
//        }
//
//        return kakaoUser;
//    }
//
//    private Member getNewMemberDataByConvertingKakaoUserToMember(KakaoUserInfoDto kakaoUserInfo) {
//        Long kakaoId = kakaoUserInfo.getId();
//        String kakaoNickname = kakaoUserInfo.getNickname();
//        // password: random UUID
//        String password = UUID.randomUUID().toString();
//        String encodedPassword = passwordEncoder.encode(password);
//        // email: kakao email
//        String kakaoEmail = kakaoUserInfo.getKakaoEmail();
//
//        return new Member(kakaoEmail, kakaoNickname, encodedPassword, kakaoId);
//    }
//
//    private HttpHeaders forceLogin(Member kakaoUser) {
//        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = String.valueOf(tokenProvider.generateTokenDto(authentication));
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//
//        return httpHeaders;
//    }
//}
//
//
