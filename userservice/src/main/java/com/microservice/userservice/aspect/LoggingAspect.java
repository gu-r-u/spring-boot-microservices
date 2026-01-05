package com.microservice.userservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.microservice.userservice.service..*(..)) || " +
            "execution(* com.microservice.userservice.controller..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();

        log.info("➡️ Entering {}", methodName);

        try {
            Object result = joinPoint.proceed();
            log.info("⬅️ Exiting {}", methodName);
            return result;

        } catch (Exception ex) {
            log.error("❌ Exception in {} : {}", methodName, ex.getMessage());
            throw ex;
        }
    }
}
