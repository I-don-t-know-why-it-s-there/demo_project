package com.example.demo.global.exception;

import com.example.demo.global.dto.CustomErrorResponseDto;
import com.example.demo.global.enums.CustomErrorCode;
import com.example.demo.global.enums.ValidErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponseDto> handleCustomException(CustomException e){
        CustomErrorCode errorCode = e.getErrorCode();

        CustomErrorResponseDto errorResponseDto = new CustomErrorResponseDto(errorCode.name(),e.getMessage());

        return new ResponseEntity<>(errorResponseDto, errorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        ValidErrorType validErrorType = ValidErrorType.of(e.getBindingResult().getFieldError().getDefaultMessage());
        CustomErrorCode errorCode = switch (validErrorType) {
            case EMAIL -> CustomErrorCode.EMAIL_INVALID_FORMAT;
            case PASSWORD -> CustomErrorCode.PASSWORD_INVALID_FORMAT;
            case USERNAME -> CustomErrorCode.USERNAME_INVALID_FORMAT;
            default -> CustomErrorCode.SERVER_ERROR;
        };

        CustomErrorResponseDto errorResponseDto = new CustomErrorResponseDto(errorCode.name(),e.getMessage());

        return new ResponseEntity<>(errorResponseDto, errorCode.getHttpStatus());
    }
}
