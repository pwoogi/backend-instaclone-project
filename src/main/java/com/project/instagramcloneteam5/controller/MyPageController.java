package com.project.instagramcloneteam5.controller;

import com.project.instagramcloneteam5.dto.auth.MemberDto;
import com.project.instagramcloneteam5.response.Response;
import com.project.instagramcloneteam5.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response findAllUsers() {
        return Response.success(myPageService.findAllUsers());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response findUser( @PathVariable Long id) {
        return Response.success(myPageService.findUser(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{id}")
    public Response editUserInfo( @PathVariable Long id, @RequestBody MemberDto userDto) {
        return Response.success(myPageService.editUserInfo(id, userDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users/{id}")
    public Response deleteUserInfo(@PathVariable Long id) {
        myPageService.deleteUserInfo(id);
        return Response.success();
    }

}