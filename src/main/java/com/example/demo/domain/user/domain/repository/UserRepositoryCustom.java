package com.example.demo.domain.user.domain.repository;

import com.example.demo.domain.user.domain.dto.UpdateUserRequestDto;
import com.example.demo.global.dto.AuthUserDto;

public interface UserRepositoryCustom {
    long updateUser(UpdateUserRequestDto requestDto, AuthUserDto userDto);
}
