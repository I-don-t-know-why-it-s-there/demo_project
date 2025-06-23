package com.example.demo.global.log;

import com.example.demo.global.constant.Const;
import com.example.demo.global.model.LogEntity;
import com.example.demo.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
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

@Component
@Aspect
@RequiredArgsConstructor
public class UserLoggingAspect extends AbstractLoggingAspect {

    private final JwtUtil jwtUtil;
    private final LogRepository logRepository;

    @Around("com.example.demo.global.log.LoggingPointcut.userLogging()")
    public Object userLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        return executeLogging(joinPoint);
    }

    @Override
    protected String extractEntityId(Class<?> targetClass, Method method, Object[] args, Object result, String logType, String serviceType) {
        if ("USER".equals(serviceType)) {
            return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                    .filter(ServletRequestAttributes.class::isInstance)
                    .map(ServletRequestAttributes.class::cast)
                    .map(ServletRequestAttributes::getRequest)
                    .map(request -> request.getHeader(Const.AUTHORIZATION_HEADER))
                    .map(tokenValue -> {
                        try {
                            String substringToken = jwtUtil.substringToken(tokenValue);
                            return jwtUtil.getUserId(substringToken).toString();
                        } catch (Exception e) {
                            logger.debug("토큰에서 사용자 ID를 추출하지 못했습니다: {}", e.getMessage());
                            return "INVALID_TOKEN";
                        }
                    })
                    .orElse("NO_TOKEN"); // 토큰 헤더가 없는 경우
        }
        return "N/A"; // USER 서비스가 아닌 경우
    }

    @Override
    protected void persistLogEntity(LogEntity logEntity, String serviceType) {
        try {
            logRepository.save(logEntity);
            logger.debug("LogEntity saved to DB for {} service: {}", serviceType, logEntity);
        } catch (Exception e) {
            logger.error("Failed to save LogEntity to DB for {} service: {}", serviceType, e.getMessage(), e);
        }
    }
}
