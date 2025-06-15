package com.example.demo.domain.user.domain.dto;

import com.example.demo.domain.user.domain.model.User;
import lombok.Getter;

@Getter
public class DeleteUserResponse {

    private final String username;

    public DeleteUserResponse(User user) {
        this.username = user.getUsername();
    }
}
