package com.example.employeedata.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Aspect
public class LoggingAspect {
    private Logger logger;
    public LoggingAspect() {
        this.logger = Logger.getLogger(LoggingAspect.class.getName());
    }

    @Around("execution(* com.example.employeedata.service.*.*(..))")
    public void log(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getName();
        String methodName = signature.getName();

        logger.info("Start of execution " + methodName + " in class " + className);

        Throwable exception = null;

        try{
            joinPoint.proceed();
        } catch (Throwable ex) {
            exception = ex;
            logger.info("Exception in " + methodName  + " in class " + className + ". Message: " + ex.getMessage());
        }

        if (exception == null) {
            logger.info("Successful execution of " + methodName + " in class " + className);
        }
    }
}
