package com.project.instagramcloneteam5.model.dto;

import com.project.instagramcloneteam5.model.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Getter
public class CommentResponseDto {
    private Long commentId;
    private String username;
    private String comment;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.username = comment.getMember().getUsername();
        this.comment = comment.getContent();
    }


}
