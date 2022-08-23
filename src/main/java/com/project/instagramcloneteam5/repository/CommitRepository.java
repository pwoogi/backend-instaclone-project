package com.project.instagramcloneteam5.repository;


import com.project.instagramcloneteam5.model.Comment;
import com.project.instagramcloneteam5.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommitRepository extends JpaRepository<Commit, Long> {
    List<Commit> findAllByComment(Comment commentId);

}
