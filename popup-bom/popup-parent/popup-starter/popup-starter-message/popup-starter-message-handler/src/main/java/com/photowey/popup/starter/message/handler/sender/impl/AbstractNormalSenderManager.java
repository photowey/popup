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
package com.photowey.popup.starter.message.handler.sender.impl;

import com.photowey.popup.starter.message.core.exception.MessageSenderNotFoundException;
import com.photowey.popup.starter.message.handler.sender.MessageSender;
import com.photowey.popup.starter.message.handler.sender.NormalSenderManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@code AbstractNormalSenderManager}
 *
 * @author photowey
 * @date 2023/10/06
 * @since 1.0.0
 */
public abstract class AbstractNormalSenderManager implements NormalSenderManager {

    protected final ConcurrentHashMap<String, MessageSender> senders = new ConcurrentHashMap<>();

    protected final Lock lock = new ReentrantLock();

    /**
     * Register {@link MessageSender}
     * <pre>
     * |- RabbitMQ
     * |- |- rabbit -> {@code RabbitMessageSender}
     * |- |- kafka -> {@code KafkaMessageSender}
     * |- |- rocket -> {@code RocketMessageSender}
     * |- ...
     * </pre>
     *
     * @param sender {@link MessageSender}
     */
    @Override
    public <T extends MessageSender> void register(T sender) {
        lock.lock();
        try {
            this.senders.computeIfAbsent(sender.name(), (x) -> sender);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public MessageSender acquire(String sender) {
        if (this.senders.containsKey(sender)) {
            return this.senders.get(sender);
        }

        throw new MessageSenderNotFoundException("MessageSender({}) not found.", sender);
    }

    @Override
    public MessageSender tryAcquire(String sender) {
        return this.senders.get(sender);
    }

    @Override
    public List<String> candidates() {
        return new ArrayList<>(this.senders.keySet());
    }

    @Override
    public List<MessageSender> senders() {
        Collection<MessageSender> values = this.senders.values();
        return List.copyOf(values);
    }
}
