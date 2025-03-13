package com.csm.trace.rocketmq;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.csm.trace.core.TraceIdContext;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

public class SendMessageTraceHook implements SendMessageHook {
    private static final TransmittableThreadLocal<String> traceHolder = new TransmittableThreadLocal<>();
    private static final String TRACE_KEY = "X-Trace-ID";

    @Override
    public String hookName() {
        return "TraceSendMessageHook";
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        if (sendMessageContext.getMessage() != null) {
            String traceId = TraceIdContext.getTraceId();
            sendMessageContext.getMessage().putUserProperty(TRACE_KEY, traceId);
            traceHolder.set(traceId);
        }
    }

    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
        traceHolder.remove();
    }
}