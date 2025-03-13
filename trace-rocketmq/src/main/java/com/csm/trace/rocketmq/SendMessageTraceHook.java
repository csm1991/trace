package com.csm.trace.rocketmq;

import com.csm.trace.core.TraceIdContext;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

public class SendMessageTraceHook implements SendMessageHook {

    @Override
    public String hookName() {
        return "TraceSendMessageHook";
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        if (sendMessageContext.getMessage() != null) {
            String traceId = TraceIdContext.getTraceId();
            sendMessageContext.getMessage().putUserProperty(TraceIdContext.TRACE_HEADER_KEY, traceId);
        }
    }

    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
        TraceIdContext.clear();
    }
}