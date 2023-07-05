package com.sparta.lv5assignment.global.exception;


import com.sparta.lv5assignment.global.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Message> nullPointExceptionHandler(NullPointerException exception) {
        Message message = new Message(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Message> nullPointExceptionHandler(IllegalArgumentException exception) {
        Message message = new Message(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Message> exceptionHandler(Exception exception) {
        Message message = new Message(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
