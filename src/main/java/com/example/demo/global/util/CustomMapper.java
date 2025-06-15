package com.example.demo.global.util;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomMapper {
    private CustomMapper() {}

    public static <T> ResponseEntity<Map<String, Object>> responseEntity(T body, HttpStatusCode status, boolean flag) {
        Map<String,Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("message", flag ? "성공" : "실패");
        map.put("data",body);
        map.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status).body(map);
    }

    public static <T, U> T toDto(U entity, Class<T> dtoClass) {
        try{
            return dtoClass.getConstructor(entity.getClass()).newInstance(entity);
        } catch (Exception e){
            throw new RuntimeException("Entity -> Dto 변환 실패");
        }
    }
}
