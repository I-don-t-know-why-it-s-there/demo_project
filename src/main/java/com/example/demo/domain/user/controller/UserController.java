package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.domain.dto.*;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.annotation.UserLogging;
import com.example.demo.global.constant.Const;
import com.example.demo.global.dto.AuthUserDto;
import com.example.demo.global.enums.UserRole;
import com.example.demo.global.util.CustomMapper;
import com.example.demo.global.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    public final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @UserLogging("회원가입")
    @PostMapping(Const.USER_CREATE_URL)
    public ResponseEntity<Map<String, Object>> createUser(
            @Valid @RequestBody CreateUserRequestDto requestDto
    ) {
        CreateUserResponseDto responseDto = userService.createUser(requestDto);
        Map<String, Object> responseEntity = CustomMapper.responseEntity(responseDto, true);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity);
    }

    //TODO 인증 인가 구현 후 키 생성 필요
    @UserLogging("로그인")
    @PostMapping(Const.USER_LOGIN_URL)
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginUserRequestDto requestDto
    ) {
        LoginUserResponseDto responseDto = userService.loginUser(requestDto);
        Map<String, Object> responseEntity = CustomMapper.responseEntity(responseDto, true);
        String token = jwtUtil.createToken(responseDto.getUserId(), responseDto.getEmail(), responseDto.getRole());

        return ResponseEntity.status(HttpStatus.OK)
                .header(Const.AUTHORIZATION_HEADER, token)
                .body(responseEntity);
    }

    //TODO 인증 인가 구현 후 AuthUserDto 객체 인증객체로 변경 필요
    @UserLogging("회원 정보 조회")
    @GetMapping(Const.USER_FIND_URL)
    public ResponseEntity<Map<String, Object>> findUser(
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        FindUserResponseDto responseDto = userService.getUser(userDto);
        Map<String, Object> responseEntity = CustomMapper.responseEntity(responseDto, true);
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity);
    }

    //TODO 인증 인가 구현 후 AuthUserDto 객체 인증객체로 변경 필요
    @UserLogging("회원 정보 수정")
    @PatchMapping(Const.USER_UPDATE_URL)
    public ResponseEntity<Map<String, Object>> updateUser(
            @Valid @RequestBody UpdateUserRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        UpdateUserResponseDto responseDto = userService.updateUser(requestDto, userDto);
        Map<String, Object> responseEntity = CustomMapper.responseEntity(responseDto, true);
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity);
    }

    //TODO 인증 인가 구현 후 AuthUserDto 객체 인증객체로 변경 필요
    @UserLogging("회원 탈퇴")
    @DeleteMapping(Const.USER_DELETE_URL)
    public ResponseEntity<Map<String, Object>> deleteUser(
            @Valid @RequestBody DeleteUserRequest requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        DeleteUserResponse responseDto = userService.deleteUser(requestDto, userDto);
        Map<String, Object> responseEntity = CustomMapper.responseEntity(responseDto, true);
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity);
    }
}
