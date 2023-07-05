package com.sparta.lv5assignment.comment.repository;

import com.sparta.lv5assignment.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
