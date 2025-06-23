package com.example.demo.global.log;

import com.example.demo.global.model.LogEntity;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Aspect
@RequiredArgsConstructor
public class PostLoggingAspect extends AbstractLoggingAspect {

    private final LogRepository logRepository;

    @Around("com.example.demo.global.log.LoggingPointcut.postLogging()")
    public Object postLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        return executeLogging(joinPoint);
    }

    @Override
    protected String extractEntityId(Class<?> targetClass, Method method, Object[] args, Object result, String logType, String serviceType) {
        if ("POST".equals(serviceType)) {
            try {
                if(result == null) {
                    for (Object arg : args) {
                        if (arg != null && arg.toString().contains("postId")) {
                            return "POST_" + arg;
                        }
                    }
                } else {
                    ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                    Object body = responseEntity.getBody();
                    if (body instanceof Map<?, ?> bodyMap) {

                        Object data = bodyMap.get("data");
                        if (data != null) {
                            Field postIdField = data.getClass().getDeclaredField("postId");
                            postIdField.setAccessible(true);
                            Object postId = postIdField.get(data);
                            return "POST_" + postId.toString();
                        }
                    }
                }
                return "POST_NEW";
            } catch (Exception e) {
                return "POST_ERROR";
            }
        }
        return "N/A";
    }

    @Override
    protected void persistLogEntity(LogEntity logEntity, String serviceType) {
        try {
            // Post 서비스 전용 로깅 로직
            // 예: Post 관련 로그를 별도 테이블에 저장하거나 특별한 처리
            logRepository.save(logEntity);
            logger.debug("Post LogEntity saved to DB for {} service: {}", serviceType, logEntity);
        } catch (Exception e) {
            logger.error("Failed to save Post LogEntity to DB for {} service: {}", serviceType, e.getMessage(), e);
        }
    }
}
