package com.csm.trace.springboot;

import com.csm.trace.core.TraceIdContext;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 拦截器，用于拦截请求，并设置traceId
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
public class TraceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(TraceIdContext.TRACE_HEADER_KEY);
        if (traceId == null || traceId.isEmpty()) {
            traceId = TraceIdContext.generateTraceId();
        }
        TraceIdContext.setTraceId(traceId);
        response.addHeader(TraceIdContext.TRACE_HEADER_KEY, traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            // 业务逻辑执行完毕
        } finally {
            TraceIdContext.clear();
        }
    }
}