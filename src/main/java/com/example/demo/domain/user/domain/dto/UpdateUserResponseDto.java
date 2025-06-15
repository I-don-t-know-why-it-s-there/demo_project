package com.example.demo.domain.user.domain.dto;

import com.example.demo.domain.user.domain.model.User;
import lombok.Getter;

@Getter
public class UpdateUserResponseDto {

    private final String email;
    private final String userName;

    public UpdateUserResponseDto(User user) {
        this.email = user.getEmail();
        this.userName = user.getUsername();
    }
}
