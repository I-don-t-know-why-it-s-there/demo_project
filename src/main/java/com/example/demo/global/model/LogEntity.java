package com.example.demo.global.model;

import jakarta.persistence.Column; // 이 임포트 추가
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter; // Setter를 추가하여 빌더 없이도 값 설정 가능하게

import java.time.LocalDateTime;

@Entity
@Getter
@Setter // @Setter 추가
@Table(name = "logs")
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String logLevel;

    @Column(length = 50)
    private String logType;

    @Column(length = 500)
    private String className;

    @Column(length = 200)
    private String methodName;

    @Column(length = 500)
    private String formatResult;

    @Column(length = 100)
    private String entityId;

    @Column(length = 4000)
    private String message;

    private LocalDateTime timestamp;
    private Long durationMs;

    @Column(length = 4000)
    private String errorMessage;

    public LogEntity(String logLevel, String logType, String className, String methodName, String formatResult, String entityId, String message, LocalDateTime timestamp, Long durationMs) {
        this.logLevel = logLevel;
        this.logType = logType;
        this.className = className;
        this.methodName = methodName;
        this.formatResult = formatResult;
        this.entityId = entityId;
        this.message = message;
        this.timestamp = timestamp;
        this.durationMs = durationMs;
    }
    public LogEntity(String logLevel, String logType, String className, String methodName, String formatResult, String entityId, String message, LocalDateTime timestamp, Long durationMs, String errorMessage) {
        this.logLevel = logLevel;
        this.logLevel = logLevel;
        this.logType = logType;
        this.className = className;
        this.methodName = methodName;
        this.formatResult = formatResult;
        this.entityId = entityId;
        this.message = message;
        this.timestamp = timestamp;
        this.durationMs = durationMs;
        this.errorMessage = errorMessage;
    }

    public LogEntity() {
    }
}