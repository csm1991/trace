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
    
    public void log(String level, String message, Object... args) {
        switch (Level.valueOf(level.toUpperCase())) {
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