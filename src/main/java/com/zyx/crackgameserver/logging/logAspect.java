package com.zyx.crackgameserver.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class logAspect {
    @Pointcut("@annotation(com.zyx.crackgameserver.logging.logAnnotation)")
    public void pt(){};

    Logger logger = LoggerFactory.getLogger(getClass());

    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long time = System.currentTimeMillis() - beginTime;

        recordLog(joinPoint,time);
        return result;
    }
    private void recordLog(ProceedingJoinPoint joinPoint,long time){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        logAnnotation logAnnotation = method.getAnnotation(com.zyx.crackgameserver.logging.logAnnotation.class);

        //请求的方法
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        logger.info("module:  {}",logAnnotation.module());
        logger.info("operator:  {}",logAnnotation.operator());

        logger.info("ClassName:  {}",className);
        logger.info(" MethodName:  {}",methodName);
        //请求的参数

    }


}
