package com.sparta.lv3assignment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotNull
    @Size(min = 4, max = 10)
    @Pattern(regexp = "[0-9a-z]+")
    private String username;

    @NotNull
    @Size(min = 8, max = 15)
    @Pattern(regexp = "[0-9a-z]+")
    private String password;

    private String authToken = "USER";
}
