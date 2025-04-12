package com.csm.trace.rocketmq.hook;

import com.csm.trace.core.TraceIdContext;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * 发送消息拦截器，用于将traceId从消息头中读取出来放置到线程变量中
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
@Component
public class ConsumerTraceHook implements ConsumeMessageHook {

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
    }
}