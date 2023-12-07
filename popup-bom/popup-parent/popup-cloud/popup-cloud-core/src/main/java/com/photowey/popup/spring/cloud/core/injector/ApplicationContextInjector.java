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
package com.photowey.popup.spring.cloud.core.injector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photowey.component.common.json.jackson.JSON;
import com.photowey.popup.spring.cloud.core.holder.ApplicationContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * {@code ApplicationContextInjector}
 *
 * @author photowey
 * @date 2023/12/07
 * @since 1.0.0
 */
public class ApplicationContextInjector implements ApplicationContextAware, BeanFactoryAware, DisposableBean, SmartInitializingSingleton {

    private ConfigurableListableBeanFactory beanFactory;
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        // @see com.photowey.popup.starter.autoconfigure.cleaner.ResourceCleaner#destroy
        // ApplicationContextHolder.clean();
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.inject();
        this.injectSharedObjectMapper();
    }

    private void inject() {
        ApplicationContextHolder.INSTANCE.applicationContext(this.applicationContext);
        ApplicationContextHolder.INSTANCE.beanFactory(this.beanFactory);
    }

    private void injectSharedObjectMapper() {
        ObjectMapper objectMapper = this.applicationContext.getBean(ObjectMapper.class);
        JSON.injectSharedObjectMapper(objectMapper);
    }
}
