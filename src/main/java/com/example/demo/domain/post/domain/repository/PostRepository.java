package com.example.demo.domain.post.domain.repository;

import com.example.demo.domain.post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
