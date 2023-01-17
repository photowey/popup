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
package com.photowey.popup.app.starting.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@code AppStartingListener}
 *
 * Handle {@link ApplicationStartingEvent}
 * ...
 *
 * @author photowey
 * @date 2023/01/01
 * @since 1.0.0
 */
@Slf4j
public abstract class AppStartingListener implements GenericApplicationListener, Ordered {

    protected static final String APP_NAME_KEY = "spring.application.name";
    protected static final Class<?>[] SOURCE_TYPES = {SpringApplication.class, ApplicationContext.class};
    protected static final Class<?>[] EVENT_TYPES = {ApplicationStartingEvent.class};

    protected static AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        this.handleStartingEvent(event);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    protected boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void handleStartingEvent(ApplicationEvent event) {
        try {
            if (event instanceof ApplicationStartingEvent && started.compareAndSet(false, true)) {
                if (StringUtils.isEmpty(System.getProperty(APP_NAME_KEY))) {
                    ApplicationStartingEvent startingEvent = (ApplicationStartingEvent) event;
                    this.handleApplicationStartingEvent(startingEvent);
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Handle the event {@code ApplicationStartingEvent}
     *
     * @param event {@link ApplicationStartingEvent}
     */
    public abstract void handleApplicationStartingEvent(ApplicationStartingEvent event);
}
