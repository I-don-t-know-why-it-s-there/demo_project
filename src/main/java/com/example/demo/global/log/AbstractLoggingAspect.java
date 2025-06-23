package com.example.demo.global.log;

import com.example.demo.global.annotation.UserLogging;
import com.example.demo.global.annotation.PostLogging;
import com.example.demo.global.model.LogEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

public abstract class AbstractLoggingAspect {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public final Object executeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String className = targetClass.getName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        String serviceType = getServiceType(method);
        
        String requestId = extractEntityId(targetClass, method, args, null, "request", serviceType);
        Level requestLogLevel = getLogLevel(className, methodName, "request", serviceType);
        String formatRequest = formatArgs(args);
        String requestMessage = String.format("[%s] [%s] 클래스: %s, 메서드: %s, 요청: %s - ID: %s",
                serviceType,
                requestLogLevel,
                className,
                methodName,
                formatRequest,
                requestId
        );
        logAtLevel(getLogLevel(className, methodName, "request", serviceType), requestMessage);

        LogEntity requestLogEntity = new LogEntity(
                "Request",
                requestLogLevel.toString(),
                className,
                methodName,
                formatRequest,
                requestId,
                requestMessage,
                LocalDateTime.now(),
                0L
        );
        persistLogEntity(requestLogEntity, serviceType);
        Object result;
        try {
            result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            String responseId = extractEntityId(targetClass, method, args, result, "response", serviceType);
            Level logLevel = getLogLevel(className, methodName, "response", serviceType);
            String formatResponse = formatResult(result);
            String responseMessage = String.format("[%s] [%s] 클래스: %s, 메서드: %s, 응답: %s (실행시간: %dms) - ID: %s",
                    serviceType,
                    logLevel,
                    className,
                    methodName,
                    formatResponse,
                    duration,
                    responseId
            );
            logAtLevel(getLogLevel(className, methodName, "response", serviceType), responseMessage);

            LogEntity responselogEntity = new LogEntity(
                    "Response",
                    logLevel.toString(),
                    className,
                    methodName,
                    formatResponse,
                    responseId,
                    responseMessage,
                    LocalDateTime.now(),
                    duration
            );
            persistLogEntity(responselogEntity, serviceType);

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            String errorId = extractEntityId(targetClass, method, args, null, "error", serviceType);
            String errorMessage = String.format("[%s] [ERROR] 클래스: %s, 메서드: %s, Error: %s (실행시간: %dms) - ID: %s",
                    serviceType,
                    className, methodName, e.getMessage(), duration, errorId);
            logAtLevel(Level.ERROR, errorMessage);

            LogEntity responselogEntity = new LogEntity(
                    "ResponseError",
                    Level.ERROR.toString(),
                    className,
                    methodName,
                    " ",
                    errorId,
                    errorMessage,
                    LocalDateTime.now(),
                    duration,
                    errorMessage
            );
            persistLogEntity(responselogEntity, serviceType);

            throw e;
        }
        return result;
    }

    private String getServiceType(Method method) {
        if (method.isAnnotationPresent(UserLogging.class)) {
            return method.getAnnotation(UserLogging.class).serviceType();
        } else if (method.isAnnotationPresent(PostLogging.class)) {
            return method.getAnnotation(PostLogging.class).serviceType();
        }
        return "UNKNOWN";
    }

    protected abstract String extractEntityId(Class<?> targetClass, Method method, Object[] args, Object result, String logType, String serviceType);

    protected abstract void persistLogEntity(LogEntity logEntity, String serviceType);

    protected Level getLogLevel(String className, String methodName, String logType, String serviceType) {
        if ("request".equals(logType) && (methodName.startsWith("get") || methodName.startsWith("find"))) {
            return Level.DEBUG;
        }
        return Level.INFO;
    };

    protected void logAtLevel(Level level, String message) {
        switch (level) {
            case DEBUG:
                logger.debug(message);
                break;
            case WARN:
                logger.warn(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            case TRACE:
                logger.trace(message);
                break;
            default:
                logger.info(message);
                break;
        }
    }

    protected String formatArgs(Object[] args) {
        return (args == null || args.length == 0) ? "[]" : Arrays.toString(args);
    }

    protected String formatResult(Object result) {
        if (result == null) return "null";
        String resStr = result.toString();
        if (resStr.length() > 200) {
            return resStr.substring(0, 200) + "...(truncated)";
        }
        return resStr;
    }
}
