package com.project.instagramcloneteam5.controller;


import com.project.instagramcloneteam5.dto.auth.LoginRequestDto;
import com.project.instagramcloneteam5.dto.auth.SignUpRequestDto;
import com.project.instagramcloneteam5.dto.auth.TokenRequestDto;
import com.project.instagramcloneteam5.dto.auth.TokenResponseDto;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.ExceptionResponseDto;
import com.project.instagramcloneteam5.response.Response;
import com.project.instagramcloneteam5.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.http.HttpHeaders;

import static com.project.instagramcloneteam5.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Response register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);
        return Response.success();

    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "*")
    public Response logIn(HttpServletResponse httpServletResponse,@Valid @RequestBody LoginRequestDto req) {
        return Response.success(memberService.logIn(req,httpServletResponse));
    }


    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(memberService.reissue(tokenRequestDto));
    }

}
