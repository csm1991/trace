package com.csm.trace.rocketmq.config;

import com.csm.trace.rocketmq.hook.ProducerTraceHook;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shumincai
 * @date 2025/4/13
 * @description
 **/
@Configuration
public class ProducerHookConfig {

    @Bean
    public Object hookRegistrar(RocketMQTemplate rocketMQTemplate,
                                ProducerTraceHook sendMessageHook) {
        rocketMQTemplate.getProducer().getDefaultMQProducerImpl()
                .registerSendMessageHook(sendMessageHook);
        return new Object();
    }
}
