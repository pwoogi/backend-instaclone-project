package com.project.instagramcloneteam5.dto.supportdto;

import com.project.instagramcloneteam5.model.Board;
import lombok.Getter;

@Getter
public class BoardUpdateResponseDto {

    private Long boardId;
    private String content;

    public BoardUpdateResponseDto(Long boardId, Board board) {
        this.boardId = boardId;
        this.content = board.getContent();
    }


}
