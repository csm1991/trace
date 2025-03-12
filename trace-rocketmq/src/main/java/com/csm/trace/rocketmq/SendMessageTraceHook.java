package com.csm.trace.rocketmq;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.csm.trace.core.TraceIdContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;

public class SendMessageTraceHook implements SendMessageHook {
    private static final TransmittableThreadLocal<String> traceHolder = new TransmittableThreadLocal<>();
    private static final String TRACE_KEY = "X-Trace-ID";

    @Override
    public String hookName() {
        return "TraceSendMessageHook";
    }

    @Override
    public void sendMessageBefore(SendMessageContext context) {
        if (context.getMessage() != null) {
            String traceId = TraceIdContext.getTraceId();
            context.getMessage().putUserProperty(TRACE_KEY, traceId);
            traceHolder.set(traceId);
        }
    }

    @Override
    public void sendMessageAfter(SendMessageContext context) {
        traceHolder.remove();
    }
}