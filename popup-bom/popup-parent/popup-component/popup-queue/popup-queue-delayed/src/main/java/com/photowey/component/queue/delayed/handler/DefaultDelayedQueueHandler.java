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
package com.photowey.component.queue.delayed.handler;

import com.photowey.component.queue.delayed.event.DelayedEvent;
import com.photowey.component.queue.delayed.listener.DelayedQueueListener;
import com.photowey.component.queue.delayed.registry.DelayedQueueListenerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * {@code DefaultDelayedQueueHandler}
 *
 * @author photowey
 * @date 2023/01/17
 * @since 1.0.0
 */
@Slf4j
public class DefaultDelayedQueueHandler implements DelayedQueueHandler, SmartInitializingSingleton {

    private final DelayQueue<DelayedEvent> delayQueue;
    private final ThreadPoolTaskExecutor delayedTaskExecutor;
    private final DelayedQueueListenerRegistry listenerRegistry;
    private final ExecutorService executorService;

    public DefaultDelayedQueueHandler(
            DelayQueue<DelayedEvent> delayQueue,
            @Qualifier("delayedTaskExecutor") ThreadPoolTaskExecutor delayedTaskExecutor,
            DelayedQueueListenerRegistry listenerRegistry
    ) {
        this.delayQueue = delayQueue;
        this.delayedTaskExecutor = delayedTaskExecutor;
        this.listenerRegistry = listenerRegistry;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.executorService.execute(this::handleEvent);
    }

    @Override
    public void handleEvent() {
        while (true) {
            try {
                if (delayQueue.size() > 0) {
                    DelayedEvent delayedEvent = delayQueue.take();
                    List<DelayedQueueListener<DelayedEvent>> delayedQueueListeners = listenerRegistry.getDelayedQueueListeners(delayedEvent);
                    AnnotationAwareOrderComparator.sort(delayedQueueListeners);

                    delayedQueueListeners.forEach(listener -> {
                        delayedTaskExecutor.submit(() -> {
                            listener.onEvent(delayedEvent);
                        });
                    });
                } else {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (Throwable e) {
                log.error("handle delayed event error", e);
            }
        }
    }
}
