package com.project.instagramcloneteam5.model;

import com.project.instagramcloneteam5.dto.supportdto.CommentRequestDto;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends AuditingFields{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany
    private List<Commit> commitList = new ArrayList<>();



    public Comment(Board board, CommentRequestDto commentRequestDto, Member member) {
        if (!StringUtils.hasText(commentRequestDto.getContent())) {
            throw new PrivateException(Code.WRONG_INPUT_COMMENT);
        }
        this.board = board;
        this.content = commentRequestDto.getContent();
        this.member = member;
    }



}
