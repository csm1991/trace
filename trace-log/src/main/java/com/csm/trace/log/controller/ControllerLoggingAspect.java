package com.csm.trace.log.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import com.csm.trace.log.config.LogProperties;
import com.csm.trace.log.util.LogUtil;
import com.csm.trace.log.util.ObjectMaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 控制器层方法执行日志切面
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-23
 */
@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    private final LogUtil logUtil;
    private final LogProperties logProperties;

    public ControllerLoggingAspect(LogUtil logUtil, LogProperties logProperties) {
        this.logUtil = logUtil;
        this.logProperties = logProperties;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RestController)")
    public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        Object[] maskedArgs = ObjectMaskUtils.maskCollections(args, logProperties.getController().getInputCollectionMask());

        long startTime = System.currentTimeMillis();
        try {
            if (logProperties.getController().getEnableInputLog()) {
                logUtil.log(logProperties.getController().getInputLevel(), 
                    "Controller method {} called with arguments: {}", methodName, maskedArgs);
            }

            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            Object maskedResult = ObjectMaskUtils.maskCollections(result, logProperties.getController().getOutputCollectionMask());

            if (logProperties.getController().getEnableExecutionLog()) {
                logUtil.log(logProperties.getController().getExecutionLevel(),
                    "Controller method {} executed in {}ms. Result: {}", methodName, executionTime, maskedResult);
            }

            long threshold = logProperties.getController().getThreshold();
            if (executionTime > threshold && logProperties.getController().getEnableTimeoutLog()) {
                logUtil.log(logProperties.getController().getTimeoutLevel(),
                    "Controller method {} executed in {}ms (exceeds threshold {}ms). Result: {}",
                    methodName, executionTime, threshold, maskedResult);
            }

            return result;
        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Controller method {} failed in {}ms with error: {}", methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }
}