/*
 * Copyright © 2022 the original author or authors (photowey@gmail.com)
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
package com.photowey.component.eventbus.guava.eventbus;

import com.photowey.component.eventbus.guava.event.Event;
import com.photowey.component.eventbus.guava.factory.NamedThreadFactory;
import com.photowey.component.eventbus.guava.subscriber.Subscriber;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * {@code GuavaEventBus}
 *
 * @author photowey
 * @date 2023/01/09
 * @since 1.0.0
 */
@Slf4j
public class GuavaEventBus implements EventBus {

    protected static final int CORE_THREAD = 1;
    protected static final int MAX_THREAD = 1;
    protected static int DEFAULT_CAPACITY = 1024 << 1;
    protected final com.google.common.eventbus.EventBus eventBus;

    public GuavaEventBus(String identifier) {
        this(identifier, false);
    }

    public GuavaEventBus(String identifier, boolean async) {
        this(identifier, DEFAULT_CAPACITY, async);
    }

    public GuavaEventBus(String identifier, int capacity, boolean async) {
        this(identifier, CORE_THREAD, MAX_THREAD, capacity, async);
    }

    public GuavaEventBus(String identifier, int corePoolSize, int maximumPoolSize, int capacity, boolean async) {
        ExecutorService eventExecutor = this.eventExecutor(identifier, corePoolSize, maximumPoolSize, capacity);
        if (async) {
            this.eventBus = new com.google.common.eventbus.AsyncEventBus(identifier, eventExecutor);
        } else {
            this.eventBus = new com.google.common.eventbus.EventBus(identifier);
        }
    }

    public GuavaEventBus(String identifier, boolean async, ExecutorService eventExecutor) {
        if (async) {
            this.eventBus = new com.google.common.eventbus.AsyncEventBus(identifier, eventExecutor);
        } else {
            this.eventBus = new com.google.common.eventbus.EventBus(identifier);
        }
    }

    @Override
    public <EVENT, T extends Subscriber<EVENT>> void register(T subscriber) {
        this.eventBus.register(subscriber);
    }

    @Override
    public <EVENT, T extends Subscriber<EVENT>> void unregister(T subscriber) {
        this.eventBus.unregister(subscriber);
    }

    @Override
    public <T extends Event> void post(T event) {
        this.eventBus.post(event);
    }

    public ExecutorService eventExecutor(String identifier, int corePoolSize, int maximumPoolSize, int capacity) {
        String nameFormat = identifier + "-%d";
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                1 << 3L,
                TimeUnit.SECONDS,
                // TODO LinkedBlockingDeque?
                new ArrayBlockingQueue<>(capacity),
                NamedThreadFactory.createNameFormat(nameFormat),
                this.rejectedExecutionHandler()
        );
    }

    public RejectedExecutionHandler rejectedExecutionHandler() {
        return (r, executor) -> {
            log.warn("eventbus executor queue is full, size:[{}], and then caller run this task", executor.getQueue().size());
            if (!executor.isShutdown()) {
                r.run();
            }
        };
    }
}
