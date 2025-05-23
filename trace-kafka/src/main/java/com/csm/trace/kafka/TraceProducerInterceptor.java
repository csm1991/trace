package com.csm.trace.kafka;

import com.csm.trace.core.TraceIdContext;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import java.util.Map;

/**
 * Kafka生产者拦截器，用于向消息中添加TraceId
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
public class TraceProducerInterceptor implements ProducerInterceptor<String, String> {

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String traceId = TraceIdContext.getTraceId();
        record.headers().add(TraceIdContext.TRACE_HEADER_KEY, traceId.getBytes());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}