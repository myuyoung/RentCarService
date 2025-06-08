package me.changwook.annotation.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Profile("test")
public class LogAspect {
    //@LogExcutitionTime 어노테이션이 붙은 메소드에 이 Advice를 적용합니다.
    @Around("@annotation(me.changwook.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime =  System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime =  System.currentTimeMillis();
        long executionTime = endTime - startTime;

        log.info("{} executed in {}ms",joinPoint.getSignature(),executionTime);
        return result;
    }
}
