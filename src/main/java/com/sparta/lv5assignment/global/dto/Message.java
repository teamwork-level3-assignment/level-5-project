package com.sparta.lv5assignment.global.dto;

import lombok.Data;

@Data
public class Message {
    private StatusEnum status;
    private String message;
    private Object data;

    public Message() {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }

    public Message(String message) {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = message;
    }
    public Message(String message, Object data) {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = data;
        this.message = message;
    }

    public Message(StatusEnum status, String message, Object data) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
