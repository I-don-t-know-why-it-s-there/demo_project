package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.domain.dto.*;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.constant.Const;
import com.example.demo.global.dto.AuthUserDto;
import com.example.demo.global.enums.UserRole;
import com.example.demo.global.util.CustomMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(Const.USER_CREATE_URL)
    public ResponseEntity<Map<String, Object>> createUser(
            @Valid @RequestBody CreateUserRequestDto requestDto
    ) {
        CreateUserResponseDto responseDto = userService.createUser(requestDto);
        return CustomMapper.responseEntity(responseDto, HttpStatus.CREATED, true);
    }

    //TODO 인증 인가 구현 후 키 생성 필요
    @PostMapping(Const.USER_LOGIN_URL)
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginUserRequestDto requestDto
    ) {
        LoginUserResponseDto responseDto = userService.loginUser(requestDto);
        return CustomMapper.responseEntity(responseDto, HttpStatus.OK, true);
    }

    //TODO 인증 인가 구현 후 AuthUserDto 객체 인증객체로 변경 필요
    @GetMapping(Const.USER_FIND_URL)
    public ResponseEntity<Map<String, Object>> findUser() {
        AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);
        FindUserResponseDto responseDto = userService.getUser(userDto);
        return CustomMapper.responseEntity(responseDto, HttpStatus.OK, true);
    }

    //TODO 인증 인가 구현 후 AuthUserDto 객체 인증객체로 변경 필요
    @PatchMapping(Const.USER_UPDATE_URL)
    public ResponseEntity<Map<String, Object>> updateUser(
            @Valid @RequestBody UpdateUserRequestDto requestDto
    ) {
        AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);
        UpdateUserResponseDto responseDto = userService.updateUser(requestDto, userDto);
        return CustomMapper.responseEntity(responseDto, HttpStatus.OK, true);
    }

    //TODO 인증 인가 구현 후 AuthUserDto 객체 인증객체로 변경 필요
    @DeleteMapping(Const.USER_DELETE_URL)
    public ResponseEntity<Map<String, Object>> deleteUser(
            @Valid @RequestBody DeleteUserRequest requestDto
    ) {
        AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);
        DeleteUserResponse responseDto = userService.deleteUser(requestDto, userDto);
        return CustomMapper.responseEntity(responseDto, HttpStatus.OK, true);
    }
}
