package com.project.instagramcloneteam5.dto.dto;

import com.project.instagramcloneteam5.model.Board;
import lombok.Getter;

import java.util.List;
@Getter
public class BoardGetResponseDto {
    private Long boardId;
    private String username;
    private String content;

    private int commentCount;

    private Long liked;

    private List<String> imgUrl;


    public BoardGetResponseDto(Long boardId, Board board, List<String> imgUrl, List<CommentResponseDto> commentList) {
        this.boardId = boardId;
        this.username = board.getMember().getUsername();
        this.content = board.getContent();
        this.imgUrl = imgUrl;
        this.commentCount = commentList.size();
    }


}

