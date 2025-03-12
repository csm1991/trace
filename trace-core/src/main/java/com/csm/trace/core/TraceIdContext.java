package com.csm.trace.core;

import org.slf4j.MDC;
import com.alibaba.ttl.TransmittableThreadLocal;
import java.util.UUID;

public class TraceIdContext {
    private static final TransmittableThreadLocal<String> traceIdHolder = new TransmittableThreadLocal<>();
    private static final String TRACE_ID_KEY = "traceId";

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void setTraceId(String traceId) {
        traceIdHolder.set(traceId);
        MDC.put(TRACE_ID_KEY, traceId);
    }

    public static String getTraceId() {
        String traceId = traceIdHolder.get();
        if (traceId == null) {
            traceId = generateTraceId();
            setTraceId(traceId);
        }
        return traceId;
    }

    public static void clear() {
        traceIdHolder.remove();
        MDC.remove(TRACE_ID_KEY);
    }
}