package com.csm.trace.rocketmq.hook;

import com.csm.trace.core.TraceIdContext;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.springframework.stereotype.Component;

/**
 * 发送消息拦截器，用于将traceId添加到消息头中
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
@Component
public class ProducerTraceHook implements SendMessageHook {

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
    }
}