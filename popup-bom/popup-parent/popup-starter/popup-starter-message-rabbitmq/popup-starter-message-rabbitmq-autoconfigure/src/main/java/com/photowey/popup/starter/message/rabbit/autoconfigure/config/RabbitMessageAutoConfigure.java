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
package com.photowey.popup.starter.message.rabbit.autoconfigure.config;

import com.photowey.popup.starter.message.rabbit.autoconfigure.callback.RabbitmqCallbackHandler;
import com.photowey.popup.starter.message.rabbit.autoconfigure.factory.ContainerFactory;
import com.photowey.popup.starter.message.rabbitmq.core.constant.RabbitConstants;
import com.photowey.popup.starter.message.rabbitmq.handler.sender.DefaultRabbitDelayedMessageSender;
import com.photowey.popup.starter.message.rabbitmq.handler.sender.DefaultRabbitMessageSender;
import com.photowey.popup.starter.message.rabbitmq.handler.sender.RabbitDelayedMessageSender;
import com.photowey.popup.starter.message.rabbitmq.handler.sender.RabbitMessageSender;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * {@code RabbitMessageAutoConfigure}
 *
 * @author photowey
 * @date 2023/10/25
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", matchIfMissing = true)
public class RabbitMessageAutoConfigure {

    /**
     * @see {@link RabbitAutoConfiguration.RabbitConnectionFactoryCreator#rabbitConnectionFactory}
     */
    @Bean
    @Primary
    public ConnectionFactory rabbitConnectionFactory(
            RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer,
            CachingConnectionFactoryConfigurer rabbitCachingConnectionFactoryConfigurer,
            ObjectProvider<ConnectionFactoryCustomizer> connectionFactoryCustomizers) throws Exception {

        RabbitConnectionFactoryBean connectionFactoryBean = new RabbitConnectionFactoryBean();
        rabbitConnectionFactoryBeanConfigurer.configure(connectionFactoryBean);
        connectionFactoryBean.afterPropertiesSet();
        com.rabbitmq.client.ConnectionFactory connectionFactory = connectionFactoryBean.getObject();
        connectionFactoryCustomizers.orderedStream()
                .forEach((customizer) -> customizer.customize(connectionFactory));

        CachingConnectionFactory factory = new CachingConnectionFactory(connectionFactory);
        rabbitCachingConnectionFactoryConfigurer.configure(factory);

        return factory;
    }

    /**
     * Object message.
     *
     * @param connectionFactory {@link ConnectionFactory}
     * @return {@link SimpleRabbitListenerContainerFactory}
     * @see {@code RabbitAutoConfiguration.RabbitConnectionFactoryCreator#rabbitConnectionFactory}
     */
    @Bean(RabbitConstants.LISTENER_CONTAINER_FACTORY_NAME)
    public SimpleRabbitListenerContainerFactory containerFactory(
            @Qualifier(RabbitConstants.RABBIT_DEFAULT_CONNECT_FACTORY_NAME) ConnectionFactory connectionFactory) {
        return ContainerFactory.containerFactory(connectionFactory);
    }

    /**
     * String message.
     *
     * @param connectionFactory {@link ConnectionFactory}
     * @return {@link SimpleRabbitListenerContainerFactory}
     */
    @Bean(RabbitConstants.STRING_LISTENER_CONTAINER_FACTORY_NAME)
    public SimpleRabbitListenerContainerFactory stringContainerFactory(
            @Qualifier(RabbitConstants.RABBIT_DEFAULT_CONNECT_FACTORY_NAME) ConnectionFactory connectionFactory) {
        return ContainerFactory.stringContainerFactory(connectionFactory);
    }

    /**
     * {@link RabbitTemplate}
     *
     * @param connectionFactory {@link ConnectionFactory}
     * @return {@link RabbitTemplate}
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean(RabbitTemplate.class)
    public RabbitTemplate rabbitTemplate(
            @Qualifier(RabbitConstants.RABBIT_DEFAULT_CONNECT_FACTORY_NAME) ConnectionFactory connectionFactory, RabbitProperties rabbitProperties) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(ContainerFactory.jackson2JsonMessageConverter());
        rabbitTemplate.setMandatory(rabbitProperties.getTemplate().getMandatory());
        rabbitTemplate.setConfirmCallback(this.rabbitmqCallbackHandler());
        rabbitTemplate.setReturnsCallback(this.rabbitmqCallbackHandler());

        return rabbitTemplate;
    }

    @Bean
    public RabbitmqCallbackHandler rabbitmqCallbackHandler() {
        return new RabbitmqCallbackHandler();
    }

    @Bean
    @ConditionalOnMissingBean(RabbitMessageSender.class)
    public RabbitMessageSender rabbitMessageSender() {
        return new DefaultRabbitMessageSender();
    }

    @Bean
    @ConditionalOnMissingBean(RabbitDelayedMessageSender.class)
    public RabbitDelayedMessageSender rabbitDelayedMessageSender() {
        return new DefaultRabbitDelayedMessageSender();
    }
}
