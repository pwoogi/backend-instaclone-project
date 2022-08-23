package com.project.instagramcloneteam5.model.dto;

import com.project.instagramcloneteam5.model.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class BoardUpdateResponseDto {

    private Long boardId;
    private String content;

    public BoardUpdateResponseDto(Long boardId, Board board) {
        this.boardId = boardId;
        this.content = board.getContent();
    }


}
