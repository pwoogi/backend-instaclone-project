package com.project.instagramcloneteam5.model;

import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn
    private Member member;

    @ManyToOne
    @JoinColumn
    private Comment comment;

    public Heart(Member member,Board board){
        this.member = member;
        this.board = board;
    }

    public Heart(Member member,Comment comment){
        this.member = member;
        this.comment = comment;
    }


}
