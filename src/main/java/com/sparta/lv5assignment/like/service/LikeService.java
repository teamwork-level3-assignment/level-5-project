package com.sparta.lv5assignment.like.service;

import com.sparta.lv5assignment.board.entity.Board;
import com.sparta.lv5assignment.board.repository.BoardRepository;
import com.sparta.lv5assignment.comment.entity.Comment;
import com.sparta.lv5assignment.comment.repository.CommentRepository;
import com.sparta.lv5assignment.global.dto.Message;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import com.sparta.lv5assignment.like.entity.Like;
import com.sparta.lv5assignment.like.repository.LikeRepository;
import com.sparta.lv5assignment.user.entity.User;
import com.sparta.lv5assignment.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시판 좋아요
     * @param user
     * @param boardId
     * @return
     */
    public Message postLikeInBoard(User user, Long boardId) {

        User findUser = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다.")
        );

        Optional<Like> like = likeRepository.findByUserAndBoardAndComment(findUser, board, null);

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return new Message(StatusEnum.OK,"게시판 좋아요 취소 완료", null);
        }else {
//            boardRepository.save(board);
            likeRepository.save(new Like(findUser, board));
            return new Message(StatusEnum.OK,"게시판 좋아요 완료", null);
        }
    }
    /**
     * 댓글 좋아요
     * @param user
     * @param boardId
     * @param commentId
     * @return
     */
    public Message postLikeInBoardInComment(User user, Long boardId, Long commentId) {

        User findUser = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 없습니다.")
        );

        Optional<Like> like = likeRepository.findByUserAndComment(findUser, comment);

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return new Message(StatusEnum.OK,"댓글 좋아요 취소 완료", null);
        }else {
            likeRepository.save(new Like(findUser, comment));
            return new Message(StatusEnum.OK,"댓글 좋아요 완료", null);
        }
    }
}
