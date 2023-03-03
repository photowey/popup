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
package com.photowey.popup.spring.cloud.gateway.app.nacos;

import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.photowey.component.common.util.LambdaUtils;
import com.photowey.component.core.api.DefaultEmpty;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * {@code DynamicNacosConfigListener}
 *
 * @author photowey
 * @date 2023/02/14
 * @since 1.0.0
 */
public class DynamicNacosConfigListener implements Listener, ApplicationContextAware {

    @Autowired
    private InMemoryRouteDefinitionRepository routeDefinitionLocator;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {
        List<RouteDefinition> updateDefinitionList = JacksonUtils.toObj(configInfo, new TypeReference<List<RouteDefinition>>() {
        });

        List<String> serviceIds = LambdaUtils.toList(updateDefinitionList, RouteDefinition::getId);
        Flux<RouteDefinition> routeDefinitions = this.routeDefinitionLocator.getRouteDefinitions();

        routeDefinitions.doOnNext(r -> {
            String serviceId = r.getId();
            if (!serviceIds.contains(serviceId)) {
                this.routeDefinitionWriter.delete(Mono.just(serviceId)).subscribeOn(Schedulers.parallel()).subscribe();
            }
        }).doOnComplete(() -> {

        }).doAfterTerminate(() -> {
            for (RouteDefinition definition : updateDefinitionList) {
                this.routeDefinitionWriter.save(Mono.just(definition)).subscribeOn(Schedulers.parallel()).subscribe();
            }
        }).subscribe();

        this.applicationContext.publishEvent(new RefreshRoutesEvent(new DefaultEmpty()));
    }
}
