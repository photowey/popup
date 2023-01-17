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
package com.photowey.component.queue.delayed.event;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * {@code AbstractDelayedEvent}
 *
 * @author photowey
 * @date 2023/01/17
 * @since 1.0.0
 */
@Data
public abstract class AbstractDelayedEvent<D> implements Serializable, DelayedEvent {

    protected String id;
    protected String topic;

    protected Long runAt;
    protected Object data;
    protected Integer enqueueTimes;

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(this.getRunAt() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed that) {
        AbstractDelayedEvent<D> delayedEvent = (AbstractDelayedEvent<D>) (that);
        long diff = this.getDelay(TimeUnit.MILLISECONDS) - (delayedEvent.getDelay(TimeUnit.MILLISECONDS));
        return (diff == 0) ? 0 : ((diff > 0) ? 1 : -1);
    }

    @Override
    public Integer getEnqueueTimes() {
        return enqueueTimes != null ? enqueueTimes : 0;
    }

    public D transfer(Function<Object, D> converter) {
        return converter.apply(this.getData());
    }

    public D transferd() {
        return (D) this.getData();
    }
}
