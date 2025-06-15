package com.example.demo.global.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 잘못되었습니다."), // (회원가입, 로그인) 이메일 형식 틀림
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호 형식이 잘못되었습니다."), // (회원가입, 로그인) 비번 형식 틀림
    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유저네임 형식이 잘못되었습니다."), // (회원가입, 로그인) 유저네임 형식 틀림
    ROLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 UserRole"),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),

    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    EMAIL_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),

    CANNOT_CHANGE_VALUE(HttpStatus.BAD_REQUEST, "변경할 수 없는 정보가 포함되어 있습니다."),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이상해씨");

    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }
}