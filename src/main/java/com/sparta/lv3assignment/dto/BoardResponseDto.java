package com.sparta.lv3assignment.dto;

import com.sparta.lv3assignment.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;                    //seq
    private String title;               //제목
    private String username;                //이름
    private String contents;            //내용
    private String passwd;              //비밀번호
    private LocalDateTime createdAt;    //생성날짜
    private LocalDateTime modifiedAt;   //수정날짜

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.passwd = board.getPasswd();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
