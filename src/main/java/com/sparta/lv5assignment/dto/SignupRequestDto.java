package com.sparta.lv5assignment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotNull
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z]).{4,10}")
    private String username;

    @NotNull
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,15}")
    private String password;
    private String authToken = "USER";
}
