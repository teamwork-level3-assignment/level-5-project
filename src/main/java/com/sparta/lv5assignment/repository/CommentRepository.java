package com.sparta.lv5assignment.repository;

import com.sparta.lv5assignment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
