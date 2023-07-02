package com.sparta.lv3assignment.service;


import com.sparta.lv3assignment.dto.CommentRequestDto;
import com.sparta.lv3assignment.dto.CommentResponseDto;
import com.sparta.lv3assignment.entity.Board;
import com.sparta.lv3assignment.entity.Comment;
import com.sparta.lv3assignment.entity.Message;
import com.sparta.lv3assignment.entity.StatusEnum;
import com.sparta.lv3assignment.jwt.JwtUtil;
import com.sparta.lv3assignment.repository.BoardRepository;
import com.sparta.lv3assignment.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest req;
    private final BoardRepository boardRepository;

    public CommentResponseDto createCommentsInBoard(Long boardId, CommentRequestDto requestDto) {

        // 누구인지 확인
        String token = jwtUtil.getTokenFromHeader(req);
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다"));

        // 댓글을 저장
        Comment comment = commentRepository.save(new Comment(username, findBoard, requestDto));
        return new CommentResponseDto(comment);
    }

    @Transactional // 변경감지
    public CommentResponseDto updateCommentInBoard(Long boardId, Long commentsId, CommentRequestDto requestDto) {

        // 누구인지 확인
        String token = jwtUtil.getTokenFromHeader(req);
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다"));

        // commentsId로 실제로 저 댓글이 존재하는지 확인하기(DB에 있는지)
        Comment comment = commentRepository.findById(commentsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (comment.getUsername().equals(username)) {
            comment.update(requestDto);
            Comment savedComment = commentRepository.saveAndFlush(comment);
            return new CommentResponseDto(savedComment);
        } else {
            throw new IllegalArgumentException("해당 댓글의 작성자가 아닙니다.");
        }
    }


    public ResponseEntity<Message> deleteCommentInBoard(Long boardId, Long commentsId) {

        // 누구인지 확인
        String token = jwtUtil.getTokenFromHeader(req);
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다"));

        // commentsId로 실제로 저 댓글이 존재하는지 확인하기(DB에 있는지)
        Comment comment = commentRepository.findById(commentsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (comment.getUsername().equals(username)) {
            try {
                commentRepository.delete(comment);
                // 성공 Message 객체 생성
                Message message = new Message(StatusEnum.OK, "해당 댓글이 삭제되었습니다.", comment);
                return new ResponseEntity<>(message, HttpStatus.OK);

            } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
                log.error(e.getMessage());
                // 실패 Message 객체 생성
                Message message = new Message(StatusEnum.NOT_FOUND, "알 수 없는 이유로 삭제할 수 없습니다.", null);
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new IllegalArgumentException("해당 댓글의 작성자가 아닙니다.");
        }
    }
}