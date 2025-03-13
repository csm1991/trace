package com.csm.trace.log.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 日志工具类
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
@Slf4j
@Component
public class LogUtil {

    @Value("${trace.log.level:INFO}")
    private String logLevel;

    private Level resolvedLevel;

    public Level getLogLevel() {
        if (resolvedLevel == null) {
            try {
                resolvedLevel = Level.valueOf(logLevel.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid log level {} detected, defaulting to INFO", logLevel);
                resolvedLevel = Level.INFO;
            }
        }
        return resolvedLevel;
    }

    public void log(String message, Object... args) {
        switch (getLogLevel()) {
            case DEBUG:
                log.debug(message, args);
                break;
            case INFO:
                log.info(message, args);
                break;
            case WARN:
                log.warn(message, args);
                break;
            case ERROR:
                log.error(message, args);
                break;
            default:
        }
    }
}