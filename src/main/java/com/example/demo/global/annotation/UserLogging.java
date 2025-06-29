package com.example.demo.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLogging {
    String value() default "No description";
    String serviceType() default "USER";  // 서비스 타입 구분
}
