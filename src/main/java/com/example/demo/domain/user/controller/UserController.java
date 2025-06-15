package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.domain.dto.CreateUserRequestDto;
import com.example.demo.domain.user.domain.dto.CreateUserResponseDto;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.constant.Const;
import com.example.demo.global.util.CustomMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(Const.USER_BASE_URL)
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(Const.USER_CREATE_URL)
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestBody CreateUserRequestDto requestDto
    ) {
        CreateUserResponseDto responseDto = userService.createUser(requestDto);
        return CustomMapper.responseEntity(responseDto, HttpStatus.CREATED, true);
    }
}
