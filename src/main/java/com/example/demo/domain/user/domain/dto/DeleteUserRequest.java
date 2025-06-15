package com.example.demo.domain.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import static com.example.demo.global.constant.ValidationMessage.PASSWORD;

@Getter
public class DeleteUserRequest {

    @NotBlank(message = PASSWORD)
    private String password;
}
