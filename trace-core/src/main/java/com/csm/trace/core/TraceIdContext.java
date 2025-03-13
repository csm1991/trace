package com.csm.trace.core;

import org.slf4j.MDC;
import com.alibaba.ttl.TransmittableThreadLocal;
import java.util.UUID;

public class TraceIdContext {

    public static final String TRACE_HEADER_KEY = "X-Trace-ID";
    public static final String TRACE_ID_KEY = "traceId";

    private static final TransmittableThreadLocal<String> TRACE_ID_HOLDER = new TransmittableThreadLocal<>();

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void setTraceId(String traceId) {
        TRACE_ID_HOLDER.set(traceId);
        MDC.put(TRACE_ID_KEY, traceId);
    }

    public static String getTraceId() {
        String traceId = TRACE_ID_HOLDER.get();
        if (traceId == null) {
            traceId = generateTraceId();
            setTraceId(traceId);
        }
        return traceId;
    }

    public static void clear() {
        TRACE_ID_HOLDER.remove();
        MDC.remove(TRACE_ID_KEY);
    }
}