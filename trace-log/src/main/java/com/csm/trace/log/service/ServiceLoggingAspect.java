package com.csm.trace.log.service;

import com.csm.trace.log.config.LogProperties;
import com.csm.trace.log.util.LogUtil;
import com.csm.trace.log.util.ObjectMaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

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
public class ServiceLoggingAspect {

    private final LogUtil logUtil;
    private final LogProperties logProperties;

    public ServiceLoggingAspect(LogUtil logUtil, LogProperties logProperties) {
        this.logUtil = logUtil;
        this.logProperties = logProperties;
    }

    @Around("@within(org.springframework.stereotype.Service) || @annotation(org.springframework.stereotype.Service)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        // 处理入参集合屏蔽
        Object[] maskedArgs = ObjectMaskUtils.maskCollections(args, logProperties.getService().getInputCollectionMask());

        long startTime = System.currentTimeMillis();
        try {
            if (logProperties.getService().getEnableInputLog()) {
                logUtil.log(logProperties.getService().getInputLevel(), "Service method {} called with arguments: {}", methodName, maskedArgs);
            }

            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            // 处理出参集合屏蔽
            Object maskedResult = ObjectMaskUtils.maskCollections(result, logProperties.getService().getOutputCollectionMask());

            if (logProperties.getService().getEnableExecutionLog()) {
                logUtil.log(logProperties.getService().getExecutionLevel(), "Service method {} executed in {}ms. Result: {}", methodName, executionTime, maskedResult);
            }

            long threshold = logProperties.getService().getThreshold();
            if (executionTime > threshold && logProperties.getService().getEnableTimeoutLog()) {
                logUtil.log(logProperties.getService().getTimeoutLevel(), "Service method {} executed in {}ms (exceeds threshold {}ms). Result: {}", methodName, executionTime, threshold, maskedResult);
            }

            return result;
        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Service method {} failed in {}ms with error: {}", methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }
}