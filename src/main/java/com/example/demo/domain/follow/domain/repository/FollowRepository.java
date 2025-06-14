package com.example.demo.domain.follow.domain.repository;

import com.example.demo.domain.follow.domain.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
