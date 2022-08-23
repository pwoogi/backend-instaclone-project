package com.project.instagramcloneteam5.model.dto;

import com.project.instagramcloneteam5.model.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentDetailsResponse {

    private Long commentId;

    private List<CommitResponseDto> commitList;

    @Builder
    public CommentDetailsResponse(Long commentId, List<CommitResponseDto> commitList) {
        this.commentId = commentId;
        this.commitList = commitList;
    }
}
