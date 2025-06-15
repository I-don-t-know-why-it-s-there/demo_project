package com.example.demo.domain.user.domain.dto;

import com.example.demo.domain.user.domain.model.User;
import com.example.demo.global.enums.UserRole;
import lombok.Getter;

@Getter
public class LoginUserResponseDto {

    private final Long userId;
    private final String email;
    private final String username;
    private final UserRole role;

    public LoginUserResponseDto(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
