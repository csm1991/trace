package com.csm.trace.feign;

import com.csm.trace.core.TraceIdContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Feign拦截器，用于传递TraceId
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
public class FeignTraceInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(TraceIdContext.TRACE_HEADER_KEY, TraceIdContext.getTraceId());
    }
}