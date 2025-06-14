package com.example.demo.global.dto;

import com.example.demo.global.enums.UserRole;
import lombok.Getter;

@Getter
public class AuthUserDto {
    private final Long id;
    private final String email;
    private final UserRole userRole;

    public AuthUserDto(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }
}
