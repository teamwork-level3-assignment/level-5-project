package com.sparta.lv4assignment.repository;

import com.sparta.lv4assignment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
