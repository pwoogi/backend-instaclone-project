package com.project.instagramcloneteam5.dto.supportdto;

import com.project.instagramcloneteam5.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
