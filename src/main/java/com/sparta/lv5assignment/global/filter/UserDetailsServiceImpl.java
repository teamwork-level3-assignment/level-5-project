package com.sparta.lv5assignment.global.filter;

import com.sparta.lv5assignment.user.entity.User;
import com.sparta.lv5assignment.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* @RestControllerAdvice 에서 @ExceptionHandler 에서 잡힐까? filter는 advice 보다 더 앞쪽에 있다고 일단 판단됨.. */
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        return new UserDetailsImpl(user);
    }
}
