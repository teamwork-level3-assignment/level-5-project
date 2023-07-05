package com.sparta.lv5assignment.like.controller;


import com.sparta.lv5assignment.global.dto.Message;
import com.sparta.lv5assignment.global.filter.UserDetailsImpl;
import com.sparta.lv5assignment.like.entity.Like;
import com.sparta.lv5assignment.like.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;
    @PostMapping("/boards/{boardId}/like")
    public ResponseEntity<Message> postLikeInBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
            ) {
        Message message = likeService.postLikeInBoard(userDetails.getUser(), boardId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}/comments/{commentId}/like")
    public ResponseEntity<Message> postLikeInBoardInComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        Message message = likeService.postLikeInBoardInComment(userDetails.getUser(), boardId, commentId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
