package com.example.demo.domain.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import static com.example.demo.global.constant.ValidationMessage.*;

@Getter
public class LoginUserRequestDto {

    @Email(message = EMAIL)
    private String email;

    @NotBlank(message = PASSWORD)
    private String password;


}
