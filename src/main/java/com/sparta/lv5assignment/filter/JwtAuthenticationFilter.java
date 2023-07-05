package com.sparta.lv5assignment.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lv5assignment.dto.LoginRequestDto;
import com.sparta.lv5assignment.entity.Message;
import com.sparta.lv5assignment.entity.StatusEnum;
import com.sparta.lv5assignment.entity.UserRoleEnum;
import com.sparta.lv5assignment.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        // 받아오기
        try {
            LoginRequestDto loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword(),
                            null
                    )
            );

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            System.out.println("JwtAuthenticationFilter.attemptAuthentication 종료");
            System.out.println("------------------------");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("--------로그인 성공-------");
        System.out.println("JwtAuthenticationFilter.successfulAuthentication 시작");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        System.out.println("authResult.getCredentials() = " + authResult.getCredentials());
        System.out.println("authResult.getAuthorities() = " + authResult.getAuthorities());

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        // 로그인 성공시 메세지
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(200);
        Message message = new Message(StatusEnum.OK, "로그인에 성공하였습니다.", null);

        response.getWriter().write(new ObjectMapper().writeValueAsString(message));


    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        // 따로 Exception 처리

    }
}
