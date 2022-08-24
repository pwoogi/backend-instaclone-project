package com.project.instagramcloneteam5.model.dto;

import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.Heart;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Getter
public class BoardGetResponseDto {
    private Long boardId;
    private String username;
    private String content;

    private int commentCount;

    private int likeCount;

    private List<String> imgUrl;


    public BoardGetResponseDto(Long boardId, Board board, List<String> imgUrl, List<CommentResponseDto> commentList) {
        this.boardId = boardId;
        this.username = board.getMember().getUsername();
        this.content = board.getContent();
        this.imgUrl = imgUrl;
        this.likeCount = board.getLikeCount();
        this.commentCount = commentList.size();
    }


}

