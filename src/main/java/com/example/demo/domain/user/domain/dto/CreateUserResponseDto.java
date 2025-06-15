package com.example.demo.domain.user.domain.dto;

import com.example.demo.domain.user.domain.model.User;
import lombok.Getter;

@Getter
public class CreateUserResponseDto {

    private final String email;
    private final String username;

    public CreateUserResponseDto(User user) {
        email = user.getEmail();
        username = user.getUsername();
    }
}
