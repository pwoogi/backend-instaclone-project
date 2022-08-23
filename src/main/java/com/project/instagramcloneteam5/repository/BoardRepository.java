package com.project.instagramcloneteam5.repository;

import com.project.instagramcloneteam5.model.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByCreatedAtDesc();
    Slice<Board> findAllBy(Pageable pageable);
}
