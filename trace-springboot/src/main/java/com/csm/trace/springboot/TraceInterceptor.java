package com.csm.trace.springboot;

import com.csm.trace.core.TraceIdContext;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TraceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader("X-Trace-ID");
        if (traceId == null || traceId.isEmpty()) {
            traceId = TraceIdContext.generateTraceId();
        }
        TraceIdContext.setTraceId(traceId);
        response.addHeader("X-Trace-ID", traceId);
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