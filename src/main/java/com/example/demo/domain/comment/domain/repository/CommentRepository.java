package com.example.demo.domain.comment.domain.repository;

import com.example.demo.domain.comment.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
