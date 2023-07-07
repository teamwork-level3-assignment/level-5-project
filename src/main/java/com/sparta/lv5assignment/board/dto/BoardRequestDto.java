package com.sparta.lv5assignment.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardRequestDto {
    private String title;       //제목
    private String contents;    //내용
    private List<String> categoryNames;
}
