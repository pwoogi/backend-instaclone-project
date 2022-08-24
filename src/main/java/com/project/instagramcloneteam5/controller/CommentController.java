package com.project.instagramcloneteam5.controller;


import com.project.instagramcloneteam5.dto.dto.CommentDetailsResponse;
import com.project.instagramcloneteam5.dto.dto.CommentRequestDto;
import com.project.instagramcloneteam5.dto.dto.CommentResponseDto;
import com.project.instagramcloneteam5.response.Response;
import com.project.instagramcloneteam5.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Comment 작성
    @PostMapping("/board/details/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response boardComment(@PathVariable(name="boardId") Long boardId, @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.boardComment(boardId, commentRequestDto);
        return Response.success(commentResponseDto);
    }


    // Commit 불러오기
    // 유저 정보 추가

    @GetMapping("/board/details/{commentId}")
    public Response getEachCommit(@PathVariable(name = "commentId") Long commentId){
        CommentDetailsResponse commentDetailsResponse = commentService.getEachCommit(commentId);
        return Response.success(commentDetailsResponse);

    }


    // Comment 삭제
    // 유저 정보 추가
    @DeleteMapping("/board/details/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return Response.success();
    }
}