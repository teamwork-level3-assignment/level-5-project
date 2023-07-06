package com.sparta.lv5assignment.comment.controller;

import com.sparta.lv5assignment.comment.dto.CommentRequestDto;
import com.sparta.lv5assignment.comment.dto.CommentResponseDto;
import com.sparta.lv5assignment.global.dto.Message;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import com.sparta.lv5assignment.global.filter.UserDetailsImpl;
import com.sparta.lv5assignment.comment.service.CommentService;
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


    @PostMapping("/boards/{boardId}/comments/{commentId}")
    public CommentResponseDto createReplyInComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.createReplyInComment(boardId, requestDto, userDetails.getUser(), commentId);
    }

    @PutMapping("/boards/{boardId}/comments/{commentsId}")
    public ResponseEntity<Message> updateCommentsInBoard(
            @PathVariable Long boardId,
            @PathVariable Long commentsId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
            CommentResponseDto commentResponseDto = commentService.updateCommentInBoard(boardId, commentsId, requestDto, userDetails.getUser());
            return new ResponseEntity<>(new Message(StatusEnum.OK, StatusEnum.OK.getCode(), commentResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentsId}")
    public ResponseEntity<Message> deleteCommentInBoard(
            @PathVariable Long boardId,
            @PathVariable Long commentsId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
            commentService.deleteCommentInBoard(boardId, commentsId, userDetails.getUser());
            return new ResponseEntity<>(new Message(StatusEnum.OK, StatusEnum.OK.getCode(), null), HttpStatus.OK);
    }

}
