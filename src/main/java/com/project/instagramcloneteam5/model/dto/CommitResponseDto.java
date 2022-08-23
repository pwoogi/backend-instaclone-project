package com.project.instagramcloneteam5.model.dto;

import com.project.instagramcloneteam5.model.Comment;
import com.project.instagramcloneteam5.model.Commit;
import lombok.Getter;

@Getter
public class CommitResponseDto {
    private Long commitId;
    private String username;
    private String content;

    public CommitResponseDto(Commit commit) {
        this.commitId = commit.getId();
        this.username = commit.getMember().getUsername();
        this.content = commit.getContent();
    }
}
