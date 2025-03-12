package com.csm.trace.kafka;

import com.csm.trace.core.TraceIdContext;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import java.util.Map;

public class TraceProducerInterceptor implements ProducerInterceptor<String, String> {
    private static final String TRACE_ID_HEADER = "X-Trace-ID";

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String traceId = TraceIdContext.getTraceId();
        record.headers().add(TRACE_ID_HEADER, traceId.getBytes());
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