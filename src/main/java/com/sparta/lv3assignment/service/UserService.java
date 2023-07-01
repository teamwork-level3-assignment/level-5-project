package com.sparta.lv3assignment.service;


import com.sparta.lv3assignment.dto.LoginRequestDto;
import com.sparta.lv3assignment.dto.SignupRequestDto;
import com.sparta.lv3assignment.entity.User;
import com.sparta.lv3assignment.entity.UserRoleEnum;
import com.sparta.lv3assignment.jwt.JwtUtil;
import com.sparta.lv3assignment.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public Map<String, Object> signup(SignupRequestDto dto) {
        Map<String, Object> map = new HashMap<>();
        String msg;
        int status;
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
            msg = "회원가입 성공";
            status = HttpServletResponse.SC_OK;
            map.put("msg", msg);
            map.put("status", status);
            return map;
        }

        throw new IllegalArgumentException("중복된 사용자가 존재합니다");
    }

    public Map<String, Object> login(LoginRequestDto dto, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String msg;
        int status;
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

        msg = "로그인 성공";
        status = HttpServletResponse.SC_OK;
        map.put("msg", msg);
        map.put("status", status);
        return map;
    }
}
