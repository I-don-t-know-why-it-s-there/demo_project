package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.dto.CreateUserRequestDto;
import com.example.demo.domain.user.domain.dto.CreateUserResponseDto;
import com.example.demo.domain.user.domain.model.User;
import com.example.demo.domain.user.domain.repository.UserRepository;
import com.example.demo.global.enums.CustomErrorCode;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.util.CustomMapper;
import com.example.demo.global.util.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto requestDto) {
        boolean existsUser = userRepository.existsByEmail(requestDto.getEmail());
        if (existsUser) {
            throw new CustomException(CustomErrorCode.EMAIL_ALREADY_EXIST);
        }

        if (!Objects.equals(requestDto.getPassword(), requestDto.getConfirmPassword())) {
            throw new CustomException(CustomErrorCode.PASSWORD_INVALID_FORMAT);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(
                requestDto.getEmail(),
                encodedPassword,
                requestDto.getUsername(),
                requestDto.getRole()
        );
        User savedUser = userRepository.save(user);

        return CustomMapper.toDto(savedUser, CreateUserResponseDto.class);
    }
}
