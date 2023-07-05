package com.sparta.lv5assignment.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String message;
    private int statusCode;

    public LoginResponseDto(String message, int status) {
        this.message = message;
        this.statusCode = status;
    }
}
