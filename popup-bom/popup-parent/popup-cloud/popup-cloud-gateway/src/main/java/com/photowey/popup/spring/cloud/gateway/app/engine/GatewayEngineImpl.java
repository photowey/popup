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
package com.photowey.popup.spring.cloud.gateway.app.engine;

import com.photowey.popup.spring.cloud.gateway.app.property.GatewayProperties;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * {@code GatewayEngineImpl}
 *
 * @author photowey
 * @date 2023/02/26
 * @since 1.0.0
 */
@Accessors(fluent = true)
public class GatewayEngineImpl implements GatewayEngine {

    @Getter
    private ListableBeanFactory beanFactory;
    @Getter
    private ApplicationContext applicationContext;
    @Getter
    private Environment environment;

    @Getter
    @Autowired
    private GatewayProperties gatewayProperties;

    @Getter
    @Autowired
    private WebClient webClient;

    @Getter
    @Autowired
    @Qualifier("commonAsyncExecutor")
    private ThreadPoolTaskExecutor commonAsyncExecutor;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
