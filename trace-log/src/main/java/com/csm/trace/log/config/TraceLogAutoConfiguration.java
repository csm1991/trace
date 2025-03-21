package com.csm.trace.log.config;

import com.csm.trace.log.service.ServiceLoggingAspect;
import com.csm.trace.log.util.LogUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置类
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
@Configuration
@ComponentScan(basePackages = "com.csm.trace.log")
@ConditionalOnProperty(prefix = "trace.log", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LogProperties.class)
public class TraceLogAutoConfiguration {

    private final LogUtil logUtil;
    private final LogProperties logProperties;

    public TraceLogAutoConfiguration(LogUtil logUtil, LogProperties logProperties) {
        this.logUtil = logUtil;
        this.logProperties = logProperties;
    }

    @Bean
    public ServiceLoggingAspect loggingAspect() {
        return new ServiceLoggingAspect(logUtil, logProperties);
    }
}