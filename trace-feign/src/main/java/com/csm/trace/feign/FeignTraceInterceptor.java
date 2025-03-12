package com.csm.trace.feign;

import com.csm.trace.core.TraceIdContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignTraceInterceptor implements RequestInterceptor {
    private static final String TRACE_HEADER = "X-Trace-ID";

    @Override
    public void apply(RequestTemplate template) {
        template.header(TRACE_HEADER, TraceIdContext.getTraceId());
    }
}