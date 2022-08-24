package com.project.instagramcloneteam5.dto.supportdto;

import com.project.instagramcloneteam5.model.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateResponseDto {

    private Long boardId;
    private String content;

    public BoardUpdateResponseDto(Long boardId, Board board) {
        this.boardId = boardId;
        this.content = board.getContent();
    }


}
