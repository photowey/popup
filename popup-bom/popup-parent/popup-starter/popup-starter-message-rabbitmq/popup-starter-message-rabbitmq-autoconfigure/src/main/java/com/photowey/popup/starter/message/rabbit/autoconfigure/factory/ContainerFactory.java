/*
 * Copyright © 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.photowey.popup.starter.message.rabbit.autoconfigure.factory;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

/**
 * {@code ContainerFactory}
 *
 * @author photowey
 * @date 2023/10/25
 * @since 1.0.0
 */
public class ContainerFactory {

    public static final int NCPU;
    private static final int PREFETCH_COUNT = 2;

    static {
        NCPU = Runtime.getRuntime().availableProcessors();
    }

    public static SimpleRabbitListenerContainerFactory containerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setMaxConcurrentConsumers(NCPU << 1);
        containerFactory.setConcurrentConsumers(NCPU);
        containerFactory.setPrefetchCount(PREFETCH_COUNT);
        containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerFactory.setMessageConverter(jackson2JsonMessageConverter());

        return containerFactory;
    }

    public static SimpleRabbitListenerContainerFactory stringContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setMaxConcurrentConsumers(NCPU << 1);
        containerFactory.setConcurrentConsumers(NCPU);
        containerFactory.setPrefetchCount(PREFETCH_COUNT);
        containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerFactory.setMessageConverter(simpleMessageConverter());

        return containerFactory;
    }

    public static Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static SimpleMessageConverter simpleMessageConverter() {
        return new SimpleMessageConverter();
    }
}