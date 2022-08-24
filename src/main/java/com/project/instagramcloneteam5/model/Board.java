package com.project.instagramcloneteam5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.instagramcloneteam5.dto.supportdto.BoardRequestDto;
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
public class Board extends AuditingFields{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @Transient
    private final List<Image> imageList = new ArrayList<>();

    private int likeCount;

    @OneToMany(mappedBy = "board")
    @JsonIgnore
    private List<Heart> heartLikeList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany
    private List<Comment> commentList = new ArrayList<>();



    public Board(String content, Member member) {
        if (!StringUtils.hasText(content)) {
            throw new PrivateException(Code.WRONG_INPUT_CONTENT);
        }
        this.content = content;
        this.member = member;
    }

    // 게시글 수정
    public void updateBoard(BoardRequestDto res) {
        if (!StringUtils.hasText(res.getContent())) {
            throw new PrivateException(Code.WRONG_INPUT_CONTENT);
        }
        this.content = res.getContent();
    }
    public void addHeartLike(Heart heart) {
        this.heartLikeList.add(heart);
    }

    public void removeHeartLike(Heart heart) {
        this.heartLikeList.remove(heart);
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
