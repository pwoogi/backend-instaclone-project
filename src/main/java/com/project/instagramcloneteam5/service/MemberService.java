package com.project.instagramcloneteam5.service;

import com.project.instagramcloneteam5.config.jwt.TokenProvider;
import com.project.instagramcloneteam5.dto.auth.*;
import com.project.instagramcloneteam5.exception.support.LoginFailureException;
import com.project.instagramcloneteam5.exception.support.MemberNotFoundException;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import com.project.instagramcloneteam5.exception.support.MemberUsernameAlreadyExistsException;
import com.project.instagramcloneteam5.model.Authority;
import com.project.instagramcloneteam5.model.Member;
import com.project.instagramcloneteam5.model.RefreshToken;
import com.project.instagramcloneteam5.repository.MemberRepository;

import com.project.instagramcloneteam5.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;




    public void signUp(SignUpRequestDto signUpRequestDto) {
        validateSignUpInfo(signUpRequestDto);

        Member member = Member.builder()
                .username(signUpRequestDto.getUsername())
                .nickname(signUpRequestDto.getNickname())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(member);

    }

    @Transactional
    public TokenGetResponseDto logIn(LoginRequestDto req, HttpServletResponse httpServletResponse) {
        Member member = memberRepository.findByUsername(req.getUsername()).orElseThrow(MemberNotFoundException::new);

        validatePassword(req, member);

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


        // 4. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 5. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        httpServletResponse.setHeader("AccessToken", "Bearer : " + tokenDto.getAccessToken());
        httpServletResponse.setHeader("ACCESS_TOKEN_EXPIRE_TIME", String.valueOf(tokenDto.getAccessTokenExpiresIn()));
        httpServletResponse.setHeader("RefreshToken", "Bearer : " + refreshToken.getValue());
        httpServletResponse.setHeader("REFRESH_TOKEN_EXPIRE_TIME", String.valueOf(tokenDto.getRefreshTokenExpiresIn()));


        TokenGetResponseDto tokenGetResponseDto = TokenGetResponseDto.builder()
                .username(req.getUsername())
                .build();


        // 5. 토큰 발급
        return tokenGetResponseDto;
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
//         1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);


        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);


        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        return tokenResponseDto;
    }


    private void validateSignUpInfo(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByUsername(signUpRequestDto.getUsername()))
            throw new MemberUsernameAlreadyExistsException(signUpRequestDto.getUsername());

    }


    private void validatePassword(LoginRequestDto loginRequestDto, Member member) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }
}
