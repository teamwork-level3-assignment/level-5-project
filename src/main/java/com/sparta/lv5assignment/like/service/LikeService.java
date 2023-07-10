package com.sparta.lv5assignment.like.service;

import com.sparta.lv5assignment.board.entity.Board;
import com.sparta.lv5assignment.board.repository.BoardRepository;
import com.sparta.lv5assignment.comment.entity.Comment;
import com.sparta.lv5assignment.comment.repository.CommentRepository;
import com.sparta.lv5assignment.global.dto.Message;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import com.sparta.lv5assignment.global.exception.CustomException;
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
    public boolean postLikeInBoard(User user, Long boardId) {

        User findUser = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_FOUND_USER)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_FOUND_BOARD)
        );

        Optional<Like> like = likeRepository.findByUserAndBoardAndComment(findUser, board, null);

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return false;
        }else {
//            boardRepository.save(board);
            likeRepository.save(new Like(findUser, board));
            return true;
        }
    }
    /**
     * 댓글 좋아요
     * @param user
     * @param boardId
     * @param commentId
     * @return
     */
    public boolean postLikeInBoardInComment(User user, Long boardId, Long commentId) {

        User findUser = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_FOUND_USER)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_FOUND_BOARD)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_FOUND_COMMENT)
        );

        Optional<Like> like = likeRepository.findByUserAndComment(findUser, comment);

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return false;
        }else {
            likeRepository.save(new Like(findUser, comment));
            return true;
        }
    }
}
