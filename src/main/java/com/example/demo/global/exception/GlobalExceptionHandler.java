package com.example.demo.global.exception;

import com.example.demo.global.dto.CustomErrorResponseDto;
import com.example.demo.global.enums.CustomErrorCode;
import com.example.demo.global.util.CustomMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException e){
        CustomErrorCode errorCode = e.getErrorCode();

        CustomErrorResponseDto errorResponseDto = new CustomErrorResponseDto(errorCode.name(),e.getMessage());
        Map<String, Object> stringObjectMap = CustomMapper.responseEntity(errorResponseDto, false);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(stringObjectMap);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        CustomErrorCode errorCode = CustomErrorCode.INVALID_REQUEST;
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();

        CustomErrorResponseDto errorResponseDto = new CustomErrorResponseDto(errorCode.name(), message);

        Map<String, Object> stringObjectMap = CustomMapper.responseEntity(errorResponseDto, false);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(stringObjectMap);
    }
}
