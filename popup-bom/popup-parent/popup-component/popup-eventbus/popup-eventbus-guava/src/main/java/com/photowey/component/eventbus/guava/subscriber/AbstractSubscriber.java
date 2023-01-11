package com.photowey.component.eventbus.guava.subscriber;

import java.util.concurrent.*;

/**
 * {@code AbstractSubscriber}
 *
 * @author photowey
 * @date 2023/01/11
 * @since 1.0.0
 */
public abstract class AbstractSubscriber<E> implements Subscriber<E> {

    protected RejectedExecutionHandler rejectedExecutionHandler;
    protected ExecutorService asyncExecutor;

    public AbstractSubscriber() {
        this.rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        this.asyncExecutor = new ThreadPoolExecutor(
                CORE_HANDLER_THREAD,
                MAX_HANDLER_THREAD,
                Integer.MAX_VALUE,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                this.threadFactory(),
                this.rejectedExecutionHandler()
        );
    }

    public AbstractSubscriber(RejectedExecutionHandler rejectedExecutionHandler, ExecutorService asyncExecutor) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        this.asyncExecutor = asyncExecutor;
    }

    @Override
    public ExecutorService asyncExecutor() {
        return this.asyncExecutor;
    }

    @Override
    public RejectedExecutionHandler rejectedExecutionHandler() {
        return this.rejectedExecutionHandler;
    }
}
