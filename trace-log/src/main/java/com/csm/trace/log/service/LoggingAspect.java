package com.csm.trace.log.service;

import com.csm.trace.log.config.LogProperties;
import com.csm.trace.log.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 服务层方法执行日志切面
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final LogUtil logUtil;
    private final LogProperties logProperties;

    public LoggingAspect(LogUtil logUtil, LogProperties logProperties) {
        this.logUtil = logUtil;
        this.logProperties = logProperties;
    }
    
    @Around("@within(org.springframework.stereotype.Service) || @annotation(org.springframework.stereotype.Service)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        
        long startTime = System.currentTimeMillis();
        try {
            logUtil.log("Method {} called with arguments: {}", methodName, args);
            
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            long threshold = logProperties.getServiceWarnThreshold();
            if (executionTime > threshold) {
                logUtil.log("Method {} executed in {}ms (exceeds threshold {}ms). Result: {}", methodName, executionTime, threshold, result);
            } else {
                logUtil.log("Method {} executed in {}ms. Result: {}", methodName, executionTime, result);
            }
            return result;
        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Method {} failed in {}ms with error: {}", methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }
}