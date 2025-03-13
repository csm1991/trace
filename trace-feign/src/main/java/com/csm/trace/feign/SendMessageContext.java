package com.csm.trace.feign;

public class SendMessageContext {
    // 消息追踪上下文实现
    private String traceId;
    
    public SendMessageContext(String traceId) {
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }
}