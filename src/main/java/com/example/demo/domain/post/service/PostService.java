package com.example.demo.domain.post.service;

import com.example.demo.domain.post.domain.dto.CreatePostRequestDto;
import com.example.demo.domain.post.domain.dto.CreatePostResponseDto;
import com.example.demo.domain.post.domain.model.Post;
import com.example.demo.domain.post.domain.repository.PostRepository;
import com.example.demo.global.dto.AuthUserDto;
import com.example.demo.global.util.CustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public CreatePostResponseDto createPost(CreatePostRequestDto requestDto, AuthUserDto userDto) {
        Post post = new Post(requestDto.getTitle(), requestDto.getContent());

        postRepository.save(post);
        return CustomMapper.toDto(post, CreatePostResponseDto.class);
    }
}
