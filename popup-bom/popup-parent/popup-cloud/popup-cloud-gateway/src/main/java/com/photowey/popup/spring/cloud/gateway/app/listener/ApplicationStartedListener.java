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
package com.photowey.popup.spring.cloud.gateway.app.listener;

import com.alibaba.fastjson2.JSON;
import com.photowey.component.core.health.Status;
import com.photowey.popup.spring.cloud.gateway.app.engine.GatewayEngine;
import com.photowey.popup.spring.cloud.gateway.app.engine.GatewayEngineAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * {@code ApplicationStartedListener}
 *
 * @author photowey
 * @date 2023/02/26
 * @since 1.0.0
 */
@Slf4j
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent>, GatewayEngineAware {

    private GatewayEngine gatewayEngine;

    @Override
    public void setGatewayEngine(GatewayEngine gatewayEngine) {
        this.gatewayEngine = gatewayEngine;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ThreadPoolTaskExecutor commonAsyncExecutor = this.gatewayEngine.commonAsyncExecutor();
        commonAsyncExecutor.execute(this::doMonoHeartbeat);
    }

    private void doMonoHeartbeat() {
        try {
            String api = this.populateHealthApi();
            Status status = this.gatewayEngine.webClient().get().uri(api).retrieve().bodyToMono(Status.class).block();
            log.info("fire heartbeat check, the server response: {}", JSON.toJSONString(status));
        } catch (Exception ignored) {
        }
    }

    private String populateHealthApi() {
        return this.gatewayEngine.gatewayProperties().getHealth().getHealthApi();
    }
}