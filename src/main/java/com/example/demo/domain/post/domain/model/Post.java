package com.example.demo.domain.post.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;

    private String content;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
