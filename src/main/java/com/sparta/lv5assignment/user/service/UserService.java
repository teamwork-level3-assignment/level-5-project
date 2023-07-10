package com.sparta.lv5assignment.user.service;


import com.sparta.lv5assignment.global.dto.StatusEnum;
import com.sparta.lv5assignment.global.exception.CustomException;
import com.sparta.lv5assignment.user.dto.LoginRequestDto;
import com.sparta.lv5assignment.user.dto.LoginResponseDto;
import com.sparta.lv5assignment.user.dto.SignupRequestDto;
import com.sparta.lv5assignment.user.dto.SignupResponseDto;
import com.sparta.lv5assignment.user.entity.User;
import com.sparta.lv5assignment.user.entity.UserRoleEnum;
import com.sparta.lv5assignment.global.jwt.JwtUtil;
import com.sparta.lv5assignment.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public ResponseEntity<SignupResponseDto> signup(SignupRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String authToken = dto.getAuthToken();

        String encodePassword = passwordEncoder.encode(password);

        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            UserRoleEnum auth = UserRoleEnum.USER;

            if (authToken.equals("ADMIN")) {
                auth = UserRoleEnum.ADMIN;
            }
            userRepository.save(new User(username, encodePassword, auth));

            return new ResponseEntity<>(new SignupResponseDto("회원가입 성공", HttpServletResponse.SC_OK), HttpStatus.OK);
        }
        throw new CustomException(StatusEnum.DUPLICATED_USER);
    }

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto dto, HttpServletResponse response) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        // username 찾기
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(StatusEnum.NOT_FOUND_USER));

        // 비밀번호 일치여부 확인
        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new CustomException(StatusEnum.PASSWORD_NOT_MATCHED);
        }

        // 토큰 만들기 -> 저장해서 보내기
        String token = jwtUtil.createToken(findUser.getUsername(), findUser.getRole());

        jwtUtil.addTwtToHeader(token, response);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));


        return new ResponseEntity<>(new LoginResponseDto("로그인 성공", HttpServletResponse.SC_OK), headers, HttpStatus.OK);
    }
}
