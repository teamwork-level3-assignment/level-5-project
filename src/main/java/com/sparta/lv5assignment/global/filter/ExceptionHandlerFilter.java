package com.sparta.lv5assignment.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lv5assignment.global.dto.Message;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("ExceptionHandlerFilter.doFilterInternal");
        try {
            filterChain.doFilter(request, response);
        } catch (UsernameNotFoundException e) {
            setErrorResponse(response, e.getMessage());
        } catch (RuntimeException e) {
            setErrorResponse(response, e.getMessage());
        } catch (Exception exception) {
            setErrorResponse(response, exception.getMessage());
        }

    }

    private void setErrorResponse(HttpServletResponse response, String msg) {
        try {
            Message message = new Message(msg);
            String messageValue = new ObjectMapper().writeValueAsString(message);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(StatusEnum.BAD_REQUEST.getStatusCode());
            response.getWriter().write(messageValue);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
