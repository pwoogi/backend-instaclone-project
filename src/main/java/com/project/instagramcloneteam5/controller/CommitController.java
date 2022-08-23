package com.project.instagramcloneteam5.controller;

import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.ExceptionResponseDto;
import com.project.instagramcloneteam5.model.dto.CommitRequestDto;
import com.project.instagramcloneteam5.model.dto.CommitResponseDto;
import com.project.instagramcloneteam5.response.Response;
import com.project.instagramcloneteam5.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommitController {
    private final CommitService commitService;

    // Commit 작성
    @PostMapping("/board/details/commit/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response boardCommit(@PathVariable(name="commentId") Long commentId, @RequestBody CommitRequestDto commitRequestDto) {
        CommitResponseDto commitResponseDto = commitService.boardCommit(commentId, commitRequestDto);
        return Response.success(commitResponseDto);
    }



    // Commit 삭제
    // 유저 정보 추가
    @DeleteMapping("/board/details/commit/{commitId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteComment(@PathVariable Long commitId) {
        commitService.deleteCommit(commitId);
        return Response.success();
    }
}
