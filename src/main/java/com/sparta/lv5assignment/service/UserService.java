package com.sparta.lv5assignment.service;


import com.sparta.lv5assignment.dto.LoginRequestDto;
import com.sparta.lv5assignment.dto.LoginResponseDto;
import com.sparta.lv5assignment.dto.SignupRequestDto;
import com.sparta.lv5assignment.dto.SignupResponseDto;
import com.sparta.lv5assignment.entity.User;
import com.sparta.lv5assignment.entity.UserRoleEnum;
import com.sparta.lv5assignment.jwt.JwtUtil;
import com.sparta.lv5assignment.repository.UserRepository;
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
        throw new IllegalArgumentException("중복된 사용자가 존재합니다");
    }

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto dto, HttpServletResponse response) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        // username 찾기
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 비밀번호 일치여부 확인
        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        // 토큰 만들기 -> 저장해서 보내기
        String token = jwtUtil.createToken(findUser.getUsername(), findUser.getRole());

        jwtUtil.addTwtToHeader(token, response);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));


        return new ResponseEntity<>(new LoginResponseDto("로그인 성공", HttpServletResponse.SC_OK), headers, HttpStatus.OK);
    }
}
