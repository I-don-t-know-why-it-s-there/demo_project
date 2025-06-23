package com.example.demo.domain.post.controller;

import com.example.demo.domain.post.domain.dto.CreatePostRequestDto;
import com.example.demo.domain.post.domain.dto.CreatePostResponseDto;
import com.example.demo.domain.post.domain.model.Post;
import com.example.demo.domain.post.service.PostService;
import com.example.demo.global.annotation.PostLogging;
import com.example.demo.global.dto.AuthUserDto;
import com.example.demo.global.util.CustomMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user-service/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostLogging("포스트 생성")
    @PostMapping
    public ResponseEntity<Map<String, Object>> post(
            @RequestBody CreatePostRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        CreatePostResponseDto responseDto = postService.createPost(requestDto, userDto);
        Map<String, Object> responseEntity = CustomMapper.responseEntity(responseDto, true);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity);
    }
}
