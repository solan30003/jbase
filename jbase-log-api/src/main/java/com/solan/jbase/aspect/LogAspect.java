package com.solan.jbase.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author: hyl
 * @date: 2020/3/30 17:46
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
//    @Pointcut("execution(public * com.encdata.*.*(..))")
    @Pointcut("@annotation(com.solan.jbase.log.annotation.LogActive) || @annotation(com.solan.jbase.log.annotation.LogActive)")
    public void LogAspect(){}

    @Before("LogAspect()")
    public void doBefore(JoinPoint joinPoint){
        System.out.println("doBefore");
    }

    @After("LogAspect()")
    public void doAfter(JoinPoint joinPoint){
        System.out.println("doAfter");
    }

    @AfterReturning("LogAspect()")
    public void doAfterReturning(JoinPoint joinPoint){
        //处理成功情况
        System.out.println("doAfterReturning");
        log.info("log--->>>doAfterReturning");
    }

    @AfterThrowing("LogAspect()")
    public void deAfterThrowing(JoinPoint joinPoint){
        //处理异常情况
        System.out.println("deAfterThrowing");
        log.info("log--->>>deAfterThrowing");
    }

    @Around("LogAspect()")
    public Object deAround(ProceedingJoinPoint joinPoint) throws Throwable{
        return joinPoint.proceed();
    }
}
