package com.project.instagramcloneteam5.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.instagramcloneteam5.response.Response;
import com.project.instagramcloneteam5.service.KakaoUserService;
import com.project.instagramcloneteam5.service.TestMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {

    private final KakaoUserService kakaoUserService;
    private final TestMemberService testMemberService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/kakao/callback")
    public Response kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        kakaoUserService.kakaoLogin(code);
        return Response.success();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members")
    public Response findAllUsers() {
        return Response.success(testMemberService.findAllUsers());
    }
}