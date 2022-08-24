package com.project.instagramcloneteam5.repository;

import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.Comment;
import com.project.instagramcloneteam5.model.Heart;
import com.project.instagramcloneteam5.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByBoardAndMember(Board board, Member member);
    Heart findByCommentAndMember(Comment comment, Member member);

    List<Heart> findByMemberAndBoardIsNotNull(Member member);
    List<Heart> findByMemberAndCommentIsNotNull(Member member);
}
