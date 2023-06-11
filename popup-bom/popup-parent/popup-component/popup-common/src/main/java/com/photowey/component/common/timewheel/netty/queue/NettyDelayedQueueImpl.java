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
package com.photowey.component.common.timewheel.netty.queue;

import com.photowey.component.common.timewheel.shared.io.netty.util.HashedWheelTimer;
import com.photowey.component.common.timewheel.shared.io.netty.util.Timer;
import com.photowey.component.common.timewheel.shared.io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * {@code NettyDelayedQueueImpl}
 *
 * <pre>
 *     // Example:
 *     NettyDelayedQueue delayQueue = new NettyDelayedQueueImpl(10L);
 * </pre>
 *
 * <pre>
 *     // Test:
 *    {@literal @T}est
 *     void testDelayedQueue() throws InterruptedException {
 *         NettyDelayedQueue delayQueue = new NettyDelayedQueueImpl(10L);
 *         HelloTaskListener listener = HelloTaskListener.create()
 *                 .data(Hello.builder().words("Hello world.111").build())
 *                 .build();
 *
 *         HelloTaskListener listener2 = HelloTaskListener.create()
 *                 .data(Hello.builder().words("Hello world.222").build())
 *                 .build();
 *
 *         delayQueue.delayMillis(listener, 200L);
 *         delayQueue.delaySeconds(listener2, 2L);
 *
 *         TimeUnit.SECONDS.sleep(7);
 *     }
 * </pre>
 *
 * @author photowey
 * @date 2023/03/30
 * @since 1.0.0
 */
@Slf4j
public class NettyDelayedQueueImpl implements NettyDelayedQueue, Serializable {

    private static final long serialVersionUID = -8407059051271730981L;

    private static final String DEFAULT_POOL_NAME = "delayed-queue";
    private static final String POOL_PREFIX = "netty";

    public static final long DEFAULT_TICK_DURATION = 100L;
    public static final long MILLISECONDS = 1000L;
    public static final int DEFAULT_TICK_SIZE = 1 << 13; // 8192

    private final Timer timer;

    public NettyDelayedQueueImpl() {
        this(buildPoolName(DEFAULT_POOL_NAME));
    }

    public NettyDelayedQueueImpl(long interval) {
        this(buildPoolName(DEFAULT_POOL_NAME), interval);
    }

    public NettyDelayedQueueImpl(int ticks, long interval) {
        this(buildPoolName(DEFAULT_POOL_NAME), ticks, interval);
    }

    public NettyDelayedQueueImpl(String poolName) {
        this(new DefaultThreadFactory(buildPoolName(poolName)));
    }

    public NettyDelayedQueueImpl(String poolName, long interval) {
        this(new DefaultThreadFactory(buildPoolName(poolName)), interval);
    }

    public NettyDelayedQueueImpl(String poolName, int ticks, long interval) {
        this(new DefaultThreadFactory(buildPoolName(poolName)), ticks, interval);
    }

    public NettyDelayedQueueImpl(ThreadFactory factory) {
        this(factory, DEFAULT_TICK_DURATION);
    }

    public NettyDelayedQueueImpl(ThreadFactory factory, long interval) {
        this(factory, DEFAULT_TICK_SIZE, interval);
    }

    public NettyDelayedQueueImpl(ThreadFactory factory, int ticks, long interval) {
        this(factory, ticks, interval, TimeUnit.MILLISECONDS);
    }

    public NettyDelayedQueueImpl(ThreadFactory factory, int ticks, long interval, TimeUnit timeUnit) {
        this.timer = new HashedWheelTimer(
                factory,
                interval,
                timeUnit,
                ticks
        );
    }

    @Override
    public <T> void delayMillis(AbstractDelayedTaskHandler<T> handler, long delayed) {
        this.delayAt(handler, delayed, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> void delaySeconds(AbstractDelayedTaskHandler<T> handler, long delayed) {
        this.delayAt(handler, delayed, TimeUnit.SECONDS);
    }

    @Override
    public <T> void delayMinutes(AbstractDelayedTaskHandler<T> handler, long delayed) {
        this.delayAt(handler, delayed, TimeUnit.MINUTES);
    }

    @Override
    public <T> void delayHours(AbstractDelayedTaskHandler<T> handler, long delayed) {
        this.delayAt(handler, delayed, TimeUnit.HOURS);
    }

    @Override
    public <T> void delayDays(AbstractDelayedTaskHandler<T> handler, long delayed) {
        this.delayAt(handler, delayed, TimeUnit.DAYS);
    }

    @Override
    public <T> boolean delayAt(AbstractDelayedTaskHandler<T> handler, long delayed, TimeUnit timeUnit) {
        if (handler == null) {
            log.error("handler is required");
            return false;
        }
        if (delayed < 0) {
            log.error("delayed must be > 0");
            return false;
        }
        if (timeUnit == null) {
            log.error("timeUnit is required");
            return false;
        }

        this.offer(handler, delayed, timeUnit);

        return true;
    }

    public <T> boolean futureAt(AbstractDelayedTaskHandler<T> handler, LocalDateTime futureAt) {
        if (handler == null) {
            log.error("handler is required");
            return false;
        }
        if (futureAt == null || futureAt.compareTo(LocalDateTime.now()) < 0) {
            log.error("futureAt is required");
            return false;
        }

        this.offer(handler, futureAt);

        return true;
    }

    private <T> void offer(AbstractDelayedTaskHandler<T> handler, LocalDateTime futureAt) {
        Task<T> task = handler.getTask();
        task.setFutureAt(futureAt);
        long now = System.currentTimeMillis();
        long delay = toTimestamp(futureAt) - now;
        this.offerz(handler, delay, TimeUnit.MILLISECONDS);
    }

    private <T> void offer(AbstractDelayedTaskHandler<T> handler, long delay, TimeUnit timeUnit) {
        Task<T> task = handler.getTask();
        task.setDelay(delay);
        task.setTimeUnit(timeUnit);

        this.offerz(handler, delay, timeUnit);
    }

    private <T> void offerz(AbstractDelayedTaskHandler<T> handler, long delay, TimeUnit timeUnit) {
        this.timer.newTimeout(handler, delay, timeUnit);
        //log.info("offer task:[{}]", handler.getTask().getId());
    }

    private static Long toTimestamp(LocalDateTime target) {
        return trimTail(target.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    private static Long trimTail(Long ts) {
        return ts / MILLISECONDS * MILLISECONDS;
    }

    private static String buildPoolName(String name) {
        return name.startsWith(POOL_PREFIX) ? name : POOL_PREFIX + "-" + name;
    }
}
