package com.sparta.lv4assignment.controller;


import com.sparta.lv4assignment.dto.LoginRequestDto;
import com.sparta.lv4assignment.dto.LoginResponseDto;
import com.sparta.lv4assignment.dto.SignupResponseDto;
import com.sparta.lv4assignment.dto.SignupRequestDto;
import com.sparta.lv4assignment.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<SignupResponseDto> signup(@Validated @RequestBody SignupRequestDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            SignupResponseDto signupResponseDto = makeFieldErrorsMessage(fieldErrors);
            return new ResponseEntity<>(signupResponseDto, HttpStatus.BAD_REQUEST);
        }
        return userService.signup(dto);
    }


//    @PostMapping("/users/login")
//    public ResponseEntity<LoginResponseDto> login(
//            HttpServletResponse response,
//            @RequestBody LoginRequestDto dto
//    ) {
//        return userService.login(dto, response);
//    }


    private SignupResponseDto makeFieldErrorsMessage(List<FieldError> fieldErrors) {
        Map<String, Object> map = new HashMap<>();
        MultiValueMap<String, String> error = new LinkedMultiValueMap<>();

        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();

            String rejectedValue = String.valueOf(fieldError.getRejectedValue());
            String defaultMessage = fieldError.getDefaultMessage();

            error.add("field", field);
            error.add("rejectedValue", rejectedValue);
            error.add("message", defaultMessage);

            String code = fieldError.getCode();
            log.info("code [{}] field : {}, Message : {}, prior value: {}", code, field, defaultMessage, rejectedValue);
        }
        map.put("error", error);
        String message = "입력 값 오류 발생";
        int statusCode = 400;
        return new SignupResponseDto(map, message, statusCode);
    }
}
