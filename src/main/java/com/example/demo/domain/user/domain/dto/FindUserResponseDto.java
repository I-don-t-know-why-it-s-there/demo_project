package com.example.demo.domain.user.domain.dto;

import com.example.demo.domain.user.domain.model.User;
import com.example.demo.global.enums.UserRole;
import jakarta.persistence.Column;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
public class FindUserResponseDto {
    private Long userId;

    private String email;

    private String username;

    private UserRole role;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;


    public FindUserResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
