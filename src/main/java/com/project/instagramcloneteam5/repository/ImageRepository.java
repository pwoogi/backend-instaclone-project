package com.project.instagramcloneteam5.repository;

import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByBoard(Board boardId);
}

