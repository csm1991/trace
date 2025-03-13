package com.csm.trace.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置属性类
 * 
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
@Data
@ConfigurationProperties(prefix = "trace.log")
public class LogProperties {
    private long serviceWarnThreshold = 1000;
}