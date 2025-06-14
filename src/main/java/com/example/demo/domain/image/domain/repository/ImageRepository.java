package com.example.demo.domain.image.domain.repository;

import com.example.demo.domain.image.domain.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
