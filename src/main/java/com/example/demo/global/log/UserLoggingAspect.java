package com.example.demo.global.log;

import com.example.demo.global.constant.Const;
import com.example.demo.global.model.LogEntity;
import com.example.demo.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // UserDetails 사용 시
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class UserLoggingAspect extends AbstractLoggingAspect {

    private final JwtUtil jwtUtil;
    private final LogRepository logRepository;

    @Override
    protected String extractEntityId(Class<?> targetClass, Method method, Object[] args, Object result, String logType) {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .map(request -> request.getHeader(Const.AUTHORIZATION_HEADER)) // "Authorization" 헤더에서 토큰 가져오기
                .map(tokenValue -> {
                    try {
                        String substringToken = jwtUtil.substringToken(tokenValue);
                        return jwtUtil.getUserId(substringToken).toString(); // Long 타입을 String으로 변환
                    } catch (Exception e) {
                        // 토큰 파싱 실패, 만료, 유효하지 않은 경우 등
                        logger.debug("토큰에서 사용자 ID를 추출하지 못했습니다: {}", e.getMessage());
                        return "INVALID_TOKEN";
                    }
                })
                .orElse("NO_TOKEN"); // 토큰 헤더가 없는 경우
    }

    @Override
    protected Level getLogLevel(String className, String methodName, String logType) {
        if ("request".equals(logType) && (methodName.startsWith("get") || methodName.startsWith("find"))) {
            return Level.DEBUG;
        }
        return Level.INFO;
    }

    @Override
    protected void persistLogEntity(LogEntity logEntity) {
        try {
            logRepository.save(logEntity);
            logger.debug("LogEntity saved to DB: {}", logEntity);
        } catch (Exception e) {
            logger.error("Failed to save LogEntity to DB: {}", e.getMessage(), e);
        }
    }
}
