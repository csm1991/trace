package com.csm.trace.rocketmq;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.csm.trace.core.TraceIdContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class ConsumeMessageTraceHook implements ConsumeMessageHook {
    private static final TransmittableThreadLocal<String> traceHolder = new TransmittableThreadLocal<>();
    private static final String TRACE_KEY = "X-Trace-ID";

    @Override
    public String hookName() {
        return "TraceConsumeMessageHook";
    }

    @Override
    public void consumeMessageBefore(ConsumeMessageContext context) {
        if (context != null && context.getMsgList() != null) {
            for (MessageExt message : context.getMsgList()) {
                String traceId = message.getUserProperty(TRACE_KEY);
                if (traceId != null && !traceId.isEmpty()) {
                    TraceIdContext.setTraceId(traceId);
                    traceHolder.set(traceId);
                }
            }
        }
    }

    @Override
    public void consumeMessageAfter(ConsumeMessageContext context) {
        traceHolder.remove();
        TraceIdContext.clear();
    }
}