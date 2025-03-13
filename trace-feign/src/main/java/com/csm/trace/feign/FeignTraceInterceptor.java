package com.csm.trace.feign;

import com.csm.trace.core.TraceIdContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignTraceInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(TraceIdContext.TRACE_HEADER_KEY, TraceIdContext.getTraceId());
    }
}