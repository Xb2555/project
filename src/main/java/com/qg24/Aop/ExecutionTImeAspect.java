package com.qg24.Aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 用于记录业务接口操作时间
 */
@Aspect//声明此为一个切面
@Component
public class ExecutionTImeAspect {
    @Around("execution(* com.qg24.controller.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();//开始时间

        Object proceed = joinPoint.proceed();//执行方法

        long endTime = System.currentTimeMillis();
        long durationTime = endTime-startTime;//获得执行时间

        System.out.println(joinPoint.getSignature()+"执行时间: "+durationTime+" ms");

        return proceed;//返回方法返回值
    }
}
