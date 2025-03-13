package com.csm.trace.rocketmq;

import com.csm.trace.core.TraceIdContext;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;

public class ConsumeMessageTraceHook implements ConsumeMessageHook {

    @Override
    public String hookName() {
        return "TraceConsumeMessageHook";
    }

    @Override
    public void consumeMessageBefore(ConsumeMessageContext context) {
        if (context != null && context.getMsgList() != null) {
            for (MessageExt message : context.getMsgList()) {
                String traceId = message.getUserProperty(TraceIdContext.TRACE_HEADER_KEY);
                if (traceId != null && !traceId.isEmpty()) {
                    TraceIdContext.setTraceId(traceId);
                }
            }
        }
    }

    @Override
    public void consumeMessageAfter(ConsumeMessageContext context) {
        TraceIdContext.clear();
    }
}