package com.csm.trace.gateway;

import com.csm.trace.core.TraceIdContext;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TraceGatewayFilter implements GatewayFilter, Ordered {
    private static final String TRACE_HEADER = "X-Trace-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(TRACE_HEADER);

        if (traceId == null || traceId.isEmpty()) {
            traceId = TraceIdContext.generateTraceId();
        }

        TraceIdContext.setTraceId(traceId);

        ServerHttpRequest modifiedRequest = request.mutate()
                .header(TRACE_HEADER, traceId)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build())
                .doFinally(signal -> TraceIdContext.clear());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}