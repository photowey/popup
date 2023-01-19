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
package com.photowey.component.queue.delayed.registry;

import com.photowey.component.queue.delayed.event.DelayedEvent;
import com.photowey.component.queue.delayed.listener.DelayedQueueListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code DefaultDelayedQueueListenerRegistry}
 *
 * @author photowey
 * @date 2023/01/17
 * @since 1.0.0
 */
public class DefaultDelayedQueueListenerRegistry implements DelayedQueueListenerRegistry {

    /**
     * K: topic
     * V: DelayedQueueListener
     */
    private static ConcurrentHashMap<String, List<DelayedQueueListener<DelayedEvent>>> LISTENER_MAP = new ConcurrentHashMap<>();

    @Override
    public void registerListener(DelayedQueueListener<DelayedEvent> delayedListener) {
        List<DelayedQueueListener<DelayedEvent>> delayedQueueListeners = LISTENER_MAP.get(delayedListener.getTopic());
        if (null == delayedQueueListeners) {
            synchronized (LISTENER_MAP) {
                delayedQueueListeners = LISTENER_MAP.get(delayedListener.getTopic());
                if (null == delayedQueueListeners) {
                    delayedQueueListeners = new ArrayList<>();
                    delayedQueueListeners.add(delayedListener);
                    LISTENER_MAP.put(delayedListener.getTopic(), delayedQueueListeners);
                }
            }
        } else {
            delayedQueueListeners.add(delayedListener);
        }
    }

    @Override
    public List<DelayedQueueListener<DelayedEvent>> getDelayedQueueListeners(DelayedEvent delayedEvent) {
        return LISTENER_MAP.get(delayedEvent.getTopic());
    }

    @Override
    public void destroy() throws Exception {
        LISTENER_MAP.clear();
        LISTENER_MAP = null;
    }
}
