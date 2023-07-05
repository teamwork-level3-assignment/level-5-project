package com.sparta.lv5assignment.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class SignupResponseDto {
    private String message;
    private int statusCode;
    private List<Map<String, Object>> fieldErrorList = new ArrayList<>();

    public SignupResponseDto(Map<String, Object> map, String message, int statusCode) {
        this.message = message;
        this.fieldErrorList.add(map);
        this.statusCode = statusCode;
    }

    public SignupResponseDto(String message, int status) {
        this.message = message;
        this.statusCode = status;
    }
}
