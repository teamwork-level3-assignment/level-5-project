package com.sparta.lv5assignment.filter;

import com.sparta.lv5assignment.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthorizationFilter.doFilterInternal");

        // 토큰 검사 로직
        String tokenFromHeader = jwtUtil.getTokenFromHeader(request);
        if (StringUtils.hasText(tokenFromHeader)) {
            String token = jwtUtil.substringToken(tokenFromHeader);


            if (!jwtUtil.validateToken(token)) {
                log.error("Token Error");
                return;
            }

            String username = jwtUtil.getUserInfoFromToken(token).getSubject();

            try {
                setAuthentication(username);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new UsernameNotFoundException(e.getMessage());
            }
        }
        filterChain.doFilter(request, response);

    }

    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication authentication = createAuthentication(username);

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
