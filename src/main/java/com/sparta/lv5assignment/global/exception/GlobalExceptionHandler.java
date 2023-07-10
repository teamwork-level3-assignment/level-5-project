package com.sparta.lv5assignment.global.exception;


import com.sparta.lv5assignment.global.dto.Message;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Message<String>> customExceptionHandler(CustomException exception) {
        return ResponseEntity
                .status(exception.getStatusEnum().getStatusCode())
                .body(Message.error(exception.getStatusEnum().name(), exception.getStatusEnum().getCode()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Message> exceptionHandler(Exception exception) {
        Message message = new Message(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
