package com.project.instagramcloneteam5.repository;

import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByBoard(Board boardId);

    Optional<Comment> findCommentByMember(String username);
}
