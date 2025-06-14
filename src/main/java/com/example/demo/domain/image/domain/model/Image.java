package com.example.demo.domain.image.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
}
