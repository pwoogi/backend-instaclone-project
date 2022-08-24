package com.project.instagramcloneteam5.dto.supportdto;

import com.project.instagramcloneteam5.dto.supportdto.CommentResponseDto;
import com.project.instagramcloneteam5.model.Board;
import lombok.Getter;

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

