package com.csm.trace.gateway;

import com.csm.trace.core.TraceIdContext;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway过滤器，用于设置请求头中的traceId
 *
 * @author Simon Cai
 * @version 1.0
 * @since 2025-03-14
 */
public class TraceGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(TraceIdContext.TRACE_HEADER_KEY);

        if (traceId == null || traceId.isEmpty()) {
            traceId = TraceIdContext.generateTraceId();
        }

        TraceIdContext.setTraceId(traceId);

        ServerHttpRequest modifiedRequest = request.mutate()
                .header(TraceIdContext.TRACE_HEADER_KEY, traceId)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build())
                .doFinally(signal -> TraceIdContext.clear());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}