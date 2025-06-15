package com.example.demo.domain.user.domain.dto;

import com.example.demo.domain.user.domain.model.User;
import com.example.demo.global.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindUserResponseDto {
    private final Long userId;

    private final String email;

    private final String username;

    private final UserRole role;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;


    public FindUserResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
