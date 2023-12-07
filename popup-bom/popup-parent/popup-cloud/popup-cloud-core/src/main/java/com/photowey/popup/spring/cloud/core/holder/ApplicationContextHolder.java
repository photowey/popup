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
package com.photowey.popup.spring.cloud.core.holder;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code ApplicationContextHolder}
 *
 * @author photowey
 * @date 2023/12/07
 * @since 1.0.0
 */
public enum ApplicationContextHolder {

    /**
     * Instance spring {@link org.springframework.context.ConfigurableApplicationContext} holder.
     */
    INSTANCE;

    /**
     * {@link org.springframework.context.ConfigurableApplicationContext}.
     */
    private ConfigurableApplicationContext applicationContext;
    private ConfigurableListableBeanFactory beanFactory;

    private final Map<Class<?>, Object> sharedObjects = new HashMap<>();

    public void applicationContext(final ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void beanFactory(final ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ConfigurableApplicationContext applicationContext() {
        return this.applicationContext;
    }

    public ConfigurableListableBeanFactory beanFactory() {
        return this.beanFactory;
    }

    public <T> T getBean(String beanName) {
        return (T) this.applicationContext.getBean(beanName);
    }

    public <T> T getBean(String beanName, Class<T> requiredType) {
        return this.applicationContext.getBean(beanName, requiredType);
    }

    public <T> void setSharedObject(Class<T> sharedType, T t) {
        this.sharedObjects.put(sharedType, t);
    }

    public <T> T getSharedObject(Class<T> sharedType) {
        Object target = this.sharedObjects.get(sharedType);
        if (null == target) {
            target = this.applicationContext.getBean(sharedType);
        }

        return (T) target;
    }

    public void cleanSharedObjects() {
        this.sharedObjects.clear();
    }

    public static void clean() {
        INSTANCE.cleanSharedObjects();
    }
}

