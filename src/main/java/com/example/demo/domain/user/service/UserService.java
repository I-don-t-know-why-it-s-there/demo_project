package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.dto.CreateUserRequestDto;
import com.example.demo.domain.user.domain.dto.CreateUserResponseDto;
import com.example.demo.domain.user.domain.dto.LoginUserRequestDto;
import com.example.demo.domain.user.domain.dto.LoginUserResponseDto;
import com.example.demo.domain.user.domain.model.User;
import com.example.demo.domain.user.domain.repository.UserRepository;
import com.example.demo.global.enums.CustomErrorCode;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.util.CustomMapper;
import com.example.demo.global.util.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public LoginUserResponseDto loginUser(LoginUserRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(CustomErrorCode.EMAIL_NOT_EXIST));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(CustomErrorCode.PASSWORD_WRONG);
        }

        return CustomMapper.toDto(user, LoginUserResponseDto.class);
    }
}
