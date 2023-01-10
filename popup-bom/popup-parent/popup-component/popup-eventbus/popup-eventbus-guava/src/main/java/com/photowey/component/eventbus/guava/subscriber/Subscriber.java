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
package com.photowey.component.eventbus.guava.subscriber;

import com.google.common.eventbus.Subscribe;
import com.photowey.component.eventbus.guava.factory.NamedThreadFactory;
import jakarta.annotation.PreDestroy;

import java.util.concurrent.*;

/**
 * {@code Subscriber}
 *
 * @author photowey
 * @date 2023/01/09
 * @since 1.0.0
 */
public interface Subscriber<E> {

    int CORE_HANDLER_THREAD = 1;
    int MAX_HANDLER_THREAD = 1;

    ExecutorService DEFAULT_EXECUTOR = new ThreadPoolExecutor(
            CORE_HANDLER_THREAD,
            MAX_HANDLER_THREAD,
            Integer.MAX_VALUE,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            NamedThreadFactory.createNameFormat("eventbus-%d")
    );

    default boolean async() {
        return true;
    }

    default void beforeEvent() {

    }

    void handleEvent(E event);

    @Subscribe
    default void onEvent(E event) {
        if (this.async()) {
            CompletableFuture.runAsync(() -> this.execute(event), this.asyncExecutor());
        } else {
            this.execute(event);
        }
    }

    default void execute(E event) {
        this.beforeEvent();
        this.handleEvent(event);
        this.afterEvent();
    }

    default void afterEvent() {

    }

    @PreDestroy
    default void onShutdown() {
        this.asyncExecutor().shutdownNow();
    }

    default ExecutorService asyncExecutor() {
        return DEFAULT_EXECUTOR;
    }
}
