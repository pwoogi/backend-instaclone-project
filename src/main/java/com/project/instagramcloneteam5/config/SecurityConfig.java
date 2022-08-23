package com.project.instagramcloneteam5.config;

import com.project.instagramcloneteam5.config.jwt.JwtAccessDeniedHandler;
import com.project.instagramcloneteam5.config.jwt.JwtAuthenticationEntryPoint;
import com.project.instagramcloneteam5.config.jwt.JwtSecurityConfig;
import com.project.instagramcloneteam5.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

import java.util.List;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;



    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB 패스워드 암호화
        return new BCryptPasswordEncoder();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf()
                .ignoringAntMatchers("/h2-console/**","/favicon.ico")
                .disable();

        // CORS
        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("*"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            cors.setAllowedHeaders(List.of("*"));

            return cors;
        });

        http
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll();


        http
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)


                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()

                .antMatchers("/api/signup", "/api/login", "/api/reissue","/h2-console/**", "/login/**", "/js/**", "/css/**", "/image/**", "/fonts/**", "/favicon.ico",
                        "/api/boards/**","/api/board/**","/api/users").permitAll()

                // TODO: 마이페이지 만들 때 혹시 추가 할 수 있는 유저정보 CRUD
                .antMatchers(HttpMethod.GET, "/api/users").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/user").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/user/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/user/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/user/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                // Kakao login
                .antMatchers(HttpMethod.POST, "/user/kakao/callback").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                // BOARD 전체조회
                .antMatchers(HttpMethod.GET, "/api/boards").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/board/write").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                // BOARD 상세조회
                .antMatchers(HttpMethod.GET, "/api/board/details/{boardId}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT,"/api/board/details/{boardId}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE,"/api/board/details/{boardId}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                // COMMENT 쓰기, 삭제
                .antMatchers(HttpMethod.POST,"/api/board/details/{boardId}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE,"/api/board/details/comment/{commentId}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                // COMMIT 쓰기, 삭제

                .antMatchers(HttpMethod.POST,"/api/board/details/commit/{commentId}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE,"/api/board/details/commit/{commitId}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")


                .anyRequest().hasAnyRole("ROLE_ADMIN")
//                .anyRequest().authenticated() // 나머지는 전부 인증 필요
//                .anyRequest().permitAll()   // 나머지는 모두 그냥 접근 가능

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
        }
}
