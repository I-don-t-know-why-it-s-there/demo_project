package com.example.demo.global.log;

import org.aspectj.lang.annotation.Pointcut;

public class LoggingPointcut {
    @Pointcut("@annotation(com.example.demo.global.annotation.UserLogging)")
    public void userLogging() {}

    @Pointcut("@annotation(com.example.demo.global.annotation.PostLogging)")
    public void postLogging() {}
}
