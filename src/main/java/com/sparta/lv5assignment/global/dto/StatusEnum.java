package com.sparta.lv5assignment.global.dto;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusEnum {
    SUCCESS(HttpStatus.OK), // 조회 관련
    CREATED_DATA(HttpStatus.CREATED),  // createXxx
    DELETE_SUCCESS(HttpStatus.NO_CONTENT, "삭제 성공"), // delete
    LIKE_SUCCESS(HttpStatus.NO_CONTENT, "좋아요"),
    LIKE_CANCEL(HttpStatus.NO_CONTENT, "좋아요 취소"),


    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND"),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "해당 사용자가 없습니다."),
    NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST, "해당 게시글이 없습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리가 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "해당 댓글이 없습니다."),
    ONLY_CREATER(HttpStatus.BAD_REQUEST, "작성자에게만 권한이 있습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "중복된 사용자 입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INTERNAL_SERER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");



    private HttpStatus statusCode;
    private String code;

    StatusEnum(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    StatusEnum(HttpStatus statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}
