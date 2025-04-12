package com.csm.trace.rocketmq.config;

import com.csm.trace.rocketmq.hook.ConsumerTraceHook;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shumincai
 * @date 2025/4/13
 * @description
 **/
@Configuration
public class ConsumerHookConfig implements BeanPostProcessor {

    private final ConsumerTraceHook consumerTraceHook;

    public ConsumerHookConfig(ConsumerTraceHook consumerTraceHook) {
        this.consumerTraceHook = consumerTraceHook;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
            container.getConsumer().getDefaultMQPushConsumerImpl()
                    .registerConsumeMessageHook(consumerTraceHook);
        }
        return bean;
    }
}
