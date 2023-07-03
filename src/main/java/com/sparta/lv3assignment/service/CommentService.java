package com.sparta.lv3assignment.service;


import com.sparta.lv3assignment.dto.CommentRequestDto;
import com.sparta.lv3assignment.dto.CommentResponseDto;
import com.sparta.lv3assignment.entity.*;
import com.sparta.lv3assignment.jwt.JwtUtil;
import com.sparta.lv3assignment.repository.BoardRepository;
import com.sparta.lv3assignment.repository.CommentRepository;
import com.sparta.lv3assignment.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest req;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final HttpServletResponse response;

    public CommentResponseDto createCommentsInBoard(Long boardId, CommentRequestDto requestDto) {

        // 누구인지 확인
        Claims info = getClaims();
        String username = info.getSubject();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("해당 사용자가 없습니다"));


        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다"));

        // 댓글을 저장
        Comment comment = commentRepository.save(new Comment(user, findBoard, requestDto));
        return new CommentResponseDto(comment);
    }

    @Transactional // 변경감지
    public CommentResponseDto updateCommentInBoard(Long boardId, Long commentsId, CommentRequestDto requestDto) {

        // 누구인지 확인
        Claims info = getClaims();
        String username = info.getSubject();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("해당 사용자가 없습니다"));

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다"));

        // commentsId로 실제로 저 댓글이 존재하는지 확인하기(DB에 있는지)
        Comment comment = commentRepository.findById(commentsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (comment.getUser().getUsername().equals(user.getUsername())
                || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            comment.update(requestDto);
            Comment savedComment = commentRepository.saveAndFlush(comment);
            return new CommentResponseDto(savedComment);
        } else {
            throw new IllegalArgumentException("해당 댓글의 작성자가 아닙니다.");
        }
    }


    public void deleteCommentInBoard(Long boardId, Long commentsId) {

        // 누구인지 확인
        Claims info = getClaims();
        String username = info.getSubject();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("해당 사용자가 없습니다"));

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다"));

        // commentsId로 실제로 저 댓글이 존재하는지 확인하기(DB에 있는지)
        Comment comment = commentRepository.findById(commentsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (comment.getUser().getUsername().equals(user.getUsername())
                || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            try {
                commentRepository.delete(comment);

            } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
                log.error(e.getMessage());
                throw new IllegalArgumentException("알 수 없는 이유로 삭제할 수 없습니다. 입력한 정보를 확인하세요");
            }
        } else {
            throw new IllegalArgumentException("해당 댓글의 작성자가 아닙니다.");
        }
    }

    private Claims getClaims() {
        String token = jwtUtil.getTokenFromHeader(req);
        token = jwtUtil.substringToken(token);
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        Claims info = jwtUtil.getUserInfoFromToken(token);
        return info;
    }
}