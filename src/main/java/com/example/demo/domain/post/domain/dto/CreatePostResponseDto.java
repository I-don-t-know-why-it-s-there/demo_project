package com.example.demo.domain.post.domain.dto;

import com.example.demo.domain.post.domain.model.Post;
import lombok.Getter;

@Getter
public class CreatePostResponseDto {

    private final Long postId;

    private final String title;

    private final String content;

    public CreatePostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
