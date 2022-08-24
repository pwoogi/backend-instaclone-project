package com.project.instagramcloneteam5.dto.supportdto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentDetailsResponse {

    private Long commentId;

    private List<CommitResponseDto> commitList;

    @Builder
    public CommentDetailsResponse(Long commentId, List<CommitResponseDto> commitList) {
        this.commentId = commentId;
        this.commitList = commitList;
    }
}
