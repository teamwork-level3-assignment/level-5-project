package com.sparta.lv3assignment.controller;

import com.sparta.lv3assignment.dto.CommentRequestDto;
import com.sparta.lv3assignment.dto.CommentResponseDto;
import com.sparta.lv3assignment.entity.Message;
import com.sparta.lv3assignment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/boards/{boardId}/comments")
    public CommentResponseDto createCommentsInBoard(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto requestDto
    ) {
        return commentService.createCommentsInBoard(boardId, requestDto);
    }

    @PutMapping("/boards/{boardId}/comments/{commentsId}")
    public CommentResponseDto updateCommentsInBoard(
            @PathVariable Long boardId,
            @PathVariable Long commentsId,
            @RequestBody CommentRequestDto requestDto
    ) {
        return commentService.updateCommentInBoard(boardId, commentsId, requestDto);
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentsId}")
    public ResponseEntity<Message> deleteCommentInBoard(
            @PathVariable Long boardId,
            @PathVariable Long commentsId
    ){
        return commentService.deleteCommentInBoard(boardId, commentsId);
    }
}
