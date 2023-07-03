package com.sparta.lv3assignment.repository;

import com.sparta.lv3assignment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
