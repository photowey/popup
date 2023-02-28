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
package com.photowey.popup.spring.cloud.gateway.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photowey.popup.spring.cloud.gateway.app.constant.GatewayConstants;
import com.photowey.popup.spring.cloud.gateway.app.engine.GatewayEngine;
import com.photowey.popup.spring.cloud.gateway.app.engine.GatewayEngineAwareBeanPostProcessor;
import com.photowey.popup.spring.cloud.gateway.app.engine.GatewayEngineImpl;
import com.photowey.popup.spring.cloud.gateway.app.filter.AuthenticateGlobalFilter;
import com.photowey.popup.spring.cloud.gateway.app.listener.ApplicationStartedListener;
import com.photowey.popup.spring.cloud.gateway.app.nacos.DynamicNacosConfigListener;
import com.photowey.popup.spring.cloud.gateway.app.nacos.NacosInstancesChangeEventSubscriber;
import com.photowey.popup.spring.cloud.gateway.app.nacos.NacosSubscriberRegister;
import com.photowey.popup.spring.cloud.gateway.app.property.GatewayProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.DefaultSslContextSpec;

import java.util.concurrent.TimeUnit;

/**
 * {@code GatewayConfigure}
 *
 * @author photowey
 * @date 2023/02/14
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(value = {GatewayProperties.class})
@Import(value = {GatewayEngineAwareBeanPostProcessor.class})
public class GatewayConfigure {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Authenticate {@link GlobalFilter}
     *
     * @return {@link AuthenticateGlobalFilter}
     */
    @Bean(GatewayConstants.GLOBAL_AUTHENTICATE_FILTER_BEAN_NAME)
    @ConditionalOnMissingBean(name = GatewayConstants.GLOBAL_AUTHENTICATE_FILTER_BEAN_NAME)
    public AuthenticateGlobalFilter authenticateGlobalFilter() {
        return new AuthenticateGlobalFilter();
    }

    @Bean
    public DynamicNacosConfigListener dynamicNacosConfigListener() {
        return new DynamicNacosConfigListener();
    }

    @Bean
    public NacosInstancesChangeEventSubscriber nacosInstancesChangeEventSubscriber() {
        return new NacosInstancesChangeEventSubscriber();
    }

    @Bean
    public NacosSubscriberRegister nacosSubscriberRegister() {
        return new NacosSubscriberRegister();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient
                .create(ConnectionProvider.builder("httpClient").build())
                .wiretap(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(5_000, TimeUnit.MILLISECONDS));
                    conn.addHandlerLast(new WriteTimeoutHandler(5_000, TimeUnit.MILLISECONDS));
                }).secure(spec -> spec.sslContext(DefaultSslContextSpec.forClient().configure((builder) -> {
                    builder.trustManager(InsecureTrustManagerFactory.INSTANCE);
                })));

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.create().mutate()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(connector)
                .exchangeStrategies(createExchangeStrategies())
                .build();
    }

    @Bean
    public ApplicationStartedListener applicationStartedListener() {
        return new ApplicationStartedListener();
    }

    @Bean
    public GatewayEngine gatewayEngine() {
        return new GatewayEngineImpl();
    }

    private ExchangeStrategies createExchangeStrategies() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().codecs(configurer -> {
            configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
            configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
            configurer.defaultCodecs().maxInMemorySize(this.toMb(16));
        }).build();

        exchangeStrategies
                .messageWriters()
                .stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

        return exchangeStrategies;
    }

    private int toMb(int unit) {
        return 1024 * 1024 * unit;
    }
}
