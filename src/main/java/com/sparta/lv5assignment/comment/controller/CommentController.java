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
    public ResponseEntity<Message<CommentResponseDto>> createCommentsInBoard(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        CommentResponseDto createdComment = commentService.createCommentsInBoard(boardId, requestDto, userDetails.getUser());
        return ResponseEntity
                .status(StatusEnum.CREATED_DATA.getStatusCode())
                .body(Message.success(StatusEnum.CREATED_DATA.name(), createdComment));
    }


    @PostMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<Message<CommentResponseDto>> createReplyInComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto createdReplyInComment = commentService.createReplyInComment(boardId, requestDto, userDetails.getUser(), commentId);
        return ResponseEntity
                .status(StatusEnum.CREATED_DATA.getStatusCode())
                .body(Message.success(StatusEnum.CREATED_DATA.name(), createdReplyInComment));
    }

    @PutMapping("/boards/{boardId}/comments/{commentsId}")
    public ResponseEntity<Message<CommentResponseDto>> updateCommentsInBoard(
            @PathVariable Long boardId,
            @PathVariable Long commentsId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
            CommentResponseDto commentResponseDto = commentService.updateCommentInBoard(boardId, commentsId, requestDto, userDetails.getUser());
        return ResponseEntity
                .status(StatusEnum.SUCCESS.getStatusCode())
                .body(Message.success(StatusEnum.SUCCESS.name(), commentResponseDto));
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentsId}")
    public ResponseEntity<Message<String>> deleteCommentInBoard(
            @PathVariable Long boardId,
            @PathVariable Long commentsId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
            commentService.deleteCommentInBoard(boardId, commentsId, userDetails.getUser());
        return ResponseEntity
                .status(StatusEnum.DELETE_SUCCESS.getStatusCode())
                .body(Message.success(StatusEnum.DELETE_SUCCESS.name(), StatusEnum.DELETE_SUCCESS.getCode()));
    }

}
