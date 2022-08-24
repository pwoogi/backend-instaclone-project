package com.project.instagramcloneteam5.dto.dto;

import com.project.instagramcloneteam5.model.Board;
import lombok.Getter;

import java.util.List;
@Getter
public class BoardDetailsResponseDto {
    private Long boardId;
    private String username;
    private String content;

    private int like;
    private List<String> imgUrl;



    private List<CommentResponseDto> commentList;

    public BoardDetailsResponseDto(Long boardId, Board board, List<String> imgUrl, List<CommentResponseDto> commentList) {
        this.boardId = boardId;
        this.username = board.getMember().getUsername();
        this.content = board.getContent();
        this.imgUrl = imgUrl;
        this.like = board.getLikeCount();
        this.commentList =commentList;
    }
}
