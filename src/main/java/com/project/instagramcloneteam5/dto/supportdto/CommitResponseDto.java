package com.project.instagramcloneteam5.dto.supportdto;

import com.project.instagramcloneteam5.model.Commit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
