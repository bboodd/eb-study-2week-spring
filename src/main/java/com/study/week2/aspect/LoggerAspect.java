package com.study.week2.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Slf4j
@Component
@Aspect
public class LoggerAspect {


    @Around("execution(* com.study.week2.controller..*Controller.*(..)) || execution(* com.study.week2.service..*Service.*(..)) || execution(* com.study.week2.mapper..*Mapper.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable{

        String name = joinPoint.getSignature().getDeclaringTypeName();
        String type =
                StringUtils.contains(name, "Controller") ? "Controller ===> " :
                StringUtils.contains(name, "Service") ? "Service ===> " :
                StringUtils.contains(name, "Mapper") ? "Mapper ===> " :
                "";

        log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }
}
