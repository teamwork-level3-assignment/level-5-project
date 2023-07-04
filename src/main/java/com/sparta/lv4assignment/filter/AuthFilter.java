package com.sparta.lv4assignment.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lv4assignment.entity.Message;
import com.sparta.lv4assignment.entity.StatusEnum;
import com.sparta.lv4assignment.entity.User;
import com.sparta.lv4assignment.jwt.JwtUtil;
import com.sparta.lv4assignment.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@Slf4j(topic = "AuthFilter")
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private static final String[] whiteUrl = {"/api/users/**", "/error", "/"};
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 전처리
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();

        if (StringUtils.hasText(url) && PatternMatchUtils.simpleMatch(whiteUrl, url)) {
            log.info("인증 처리를 하지 않는 url: {}", url);
            // 회원가입, 로그인 관련 API 는 인증 필요없이 요청 진행
            chain.doFilter(req, res);
        } else {
            // 나머지 API 요청은 인증 처리 진행
            // 토큰 확인;
            String tokenValue = jwtUtil.getTokenFromHeader(req);

            if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(jwtUtil.BEARER_PREFIX)) {
                // JWT 토큰 substring
                String token = jwtUtil.substringToken(tokenValue);

                // 토큰 검증
                if (!jwtUtil.validateToken(token)) {
                    // 토큰이 유효하지 않음.
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    res.setCharacterEncoding("utf-8");
                    res.setStatus(400);
                    Message message = new Message(StatusEnum.BAD_REQUEST, "토큰이 유효하지 않습니다.", null);
                    res.getWriter().write(new ObjectMapper().writeValueAsString(message));
                    return;
                }

                // 토큰에서 사용자 정보 가져오기
                Claims info = jwtUtil.getUserInfoFromToken(token);

                User findUser = userRepository.findByUsername(
                                info.getSubject())
                        .orElseThrow(() -> new IllegalArgumentException("Not Found User")
                        );

//                req.setAttribute("user", findUser);
                chain.doFilter(req, res);

            } else {
                // 토큰을 전달하지 않음
                System.out.println(" ================== 여기로옴 ");
                res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                res.setCharacterEncoding("utf-8");
                res.setStatus(400);
                Message message = new Message(StatusEnum.BAD_REQUEST, "토큰이 유효하지 않습니다.", null);

                res.getWriter().write(new ObjectMapper().writeValueAsString(message));
            }

        }

    }
}
