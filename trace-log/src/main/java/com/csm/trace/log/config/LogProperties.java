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
    private ServiceProperties service = new ServiceProperties();

    @Data
    public static class ServiceProperties {
        private long threshold = 1000;
        private String inputLevel = "INFO";
        private String executionLevel = "INFO";
        private String timeoutLevel = "WARN";
        /**
         * 是否对入参集合类型进行屏蔽（true-屏蔽集合内容，只保留类型信息）
         */
        private Boolean inputCollectionMask = false;
        /**
         * 是否对出参集合类型进行屏蔽（true-屏蔽集合内容，只保留类型信息）
         */
        private Boolean outputCollectionMask = false;
    }

    @Data
    public static class MapperProperties {
        private long threshold = 1000;
        private String inputLevel = "INFO";
        private String executionLevel = "INFO";
        private String timeoutLevel = "WARN";
    }

    private MapperProperties mapper = new MapperProperties();
}