package com.example.demo.global.util;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Responser {
    public static <T> ResponseEntity<Map<String, Object>> responseEntity(T body, HttpStatusCode status) {
        Map<String,Object> map = new HashMap<>();
        map.put("success", true);
        map.put("message", "성공");
        map.put("data",body);
        map.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status).body(map);
    }
}
