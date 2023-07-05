package com.sparta.lv5assignment.like.repository;

import com.sparta.lv5assignment.board.entity.Board;
import com.sparta.lv5assignment.comment.entity.Comment;
import com.sparta.lv5assignment.like.entity.Like;
import com.sparta.lv5assignment.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
//    Optional<Like> findByUserAndBoard(User findUser, Board board);

    Optional<Like> findByUserAndBoardAndComment(User findUser, Board board, Comment comment);

    Optional<Like> findByUserAndComment(User findUser, Comment comment);
}
