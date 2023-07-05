package com.sparta.lv5assignment.board.dto;

import com.sparta.lv5assignment.comment.dto.CommentResponseDto;
import com.sparta.lv5assignment.board.entity.Board;
import com.sparta.lv5assignment.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;                    //seq
    private String title;               //제목
    private String username;            //이름
    private String contents;            //내용
    private LocalDateTime createdAt;    //생성날짜
    private LocalDateTime modifiedAt;   //수정날짜
    private int likes;

    private List<CommentResponseDto> commentList = new ArrayList<>();

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUser().getUsername();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.likes = board.getLikeList().size();
        for (Comment comment : board.getCommentList()) {
            commentList.add(new CommentResponseDto(comment));
        }
    }

}
