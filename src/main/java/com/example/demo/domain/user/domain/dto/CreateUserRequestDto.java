package com.example.demo.domain.user.domain.dto;

import com.example.demo.global.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.demo.global.constant.ValidationMessage.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    @Email(message = EMAIL)
    private String email;
    @NotBlank(message = PASSWORD)
    private String password;
    @NotBlank(message = PASSWORD)
    private String confirmPassword;
    @NotBlank(message = USERNAME)
    private String username;

    private UserRole role = UserRole.USER;


}
