package com.csm.trace.log.mapper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import com.csm.trace.log.config.LogProperties;
import com.csm.trace.log.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.csm.trace.log.util.ObjectMaskUtils;

/**
 * 拦截MyBatis-Plus的SQL执行的切面类
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
@Aspect
@Component
@Slf4j
public class MapperLoggingAspect {

    private final LogUtil logUtil;
    private final LogProperties logProperties;

    public MapperLoggingAspect(LogUtil logUtil, LogProperties logProperties) {
        this.logUtil = logUtil;
        this.logProperties = logProperties;
    }

    @Around("execution(* com.baomidou.mybatisplus.core.mapper.BaseMapper.*(..))")
    public Object logSqlExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        // 处理入参集合屏蔽
        Object[] maskedArgs = ObjectMaskUtils.maskCollections(args, logProperties.getMapper().getInputCollectionMask());

        long startTime = System.currentTimeMillis();
        try {
            logUtil.log(logProperties.getMapper().getInputLevel(), "Mapper method {} called with arguments: {}", methodName, maskedArgs);

            Object result = joinPoint.proceed();
            
            // 处理出参集合屏蔽
            Object maskedResult = ObjectMaskUtils.maskCollections(result, logProperties.getMapper().getOutputCollectionMask());
            long executionTime = System.currentTimeMillis() - startTime;

            logUtil.log(logProperties.getMapper().getExecutionLevel(), "Mapper method {} executed in {}ms. Result: {}", methodName, executionTime, maskedResult);

            long threshold = logProperties.getMapper().getThreshold();
            if (executionTime > threshold) {
                logUtil.log(logProperties.getMapper().getTimeoutLevel(), "Mapper method {} executed in {}ms (exceeds threshold {}ms). Result: {}", methodName, executionTime, threshold, maskedResult);
            }

            return result;
        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Mapper method {} failed in {}ms with error: {}", methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }
}