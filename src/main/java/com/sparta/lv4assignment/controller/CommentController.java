package com.sparta.lv4assignment.controller;

import com.sparta.lv4assignment.dto.CommentRequestDto;
import com.sparta.lv4assignment.dto.CommentResponseDto;
import com.sparta.lv4assignment.entity.Message;
import com.sparta.lv4assignment.entity.StatusEnum;
import com.sparta.lv4assignment.filter.UserDetailsImpl;
import com.sparta.lv4assignment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/boards/{boardId}/comments")
    public CommentResponseDto createCommentsInBoard(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return commentService.createCommentsInBoard(boardId, requestDto, userDetails.getUser());
    }

    @PutMapping("/boards/{boardId}/comments/{commentsId}")
    public ResponseEntity<Message> updateCommentsInBoard(
            @PathVariable Long boardId,
            @PathVariable Long commentsId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            CommentResponseDto commentResponseDto = commentService.updateCommentInBoard(boardId, commentsId, requestDto, userDetails.getUser());
            return new ResponseEntity<>(new Message(StatusEnum.OK, StatusEnum.OK.getCode(), commentResponseDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(StatusEnum.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentsId}")
    public ResponseEntity<Message> deleteCommentInBoard(
            @PathVariable Long boardId,
            @PathVariable Long commentsId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            commentService.deleteCommentInBoard(boardId, commentsId, userDetails.getUser());
            return new ResponseEntity<>(new Message(StatusEnum.OK, StatusEnum.OK.getCode(), null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(StatusEnum.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
