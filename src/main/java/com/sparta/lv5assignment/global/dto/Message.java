package com.sparta.lv5assignment.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message<T> {

    private String message;
    private T data;

    public static <T> Message<T> error(String message, T statusEnum) {
        return new Message<>(message, statusEnum);      // 에러발생 시, 에러코드(HttpStatusCode) 와 메세지
    }

    public static <T> Message<T> success(String message, T data) {
        return new Message<>(message, data);      // 우리가 성공했을때 반환해 주어야 하는 것들.
    }
}
