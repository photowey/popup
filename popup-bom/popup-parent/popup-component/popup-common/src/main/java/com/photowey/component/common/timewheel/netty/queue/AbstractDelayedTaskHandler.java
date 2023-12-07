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

import com.alibaba.fastjson2.JSON;
import com.photowey.component.common.timewheel.shared.io.netty.util.Timeout;
import com.photowey.component.common.timewheel.shared.io.netty.util.TimerTask;
import com.photowey.component.common.timewheel.shared.org.springframework.util.AlternativeJdkIdGenerator;
import com.photowey.component.common.timewheel.shared.org.springframework.util.IdGenerator;
import com.photowey.component.common.timewheel.shared.org.springframework.util.StopWatch;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * {@code AbstractDelayedTaskHandler}
 *
 * @author photowey
 * @date 2023/03/30
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractDelayedTaskHandler<T> implements TimerTask {

    private static final String GROUP = "delayed-queue-group";
    private final IdGenerator generator = new AlternativeJdkIdGenerator();

    @Getter
    @Setter
    private Task<T> task;

    @Getter
    @Setter
    private T data;

    @Override
    public void run(Timeout timeout) throws Exception {
        try {
            StopWatch watch = new StopWatch(GROUP);
            watch.start("t1");
            if (log.isDebugEnabled()) {
                log.debug("Handle.prepare run the.netty.delayed.queue.task:[{}]", task.getId());
            }
            this.handle(task);
            watch.stop();
            if (log.isInfoEnabled()) {
                log.info("Handle.post run the.netty.delayed.queue.task:[{}], cast:[{} ms]", task.getId(), watch.getTotalTimeMillis());
            }
        } catch (Throwable e) {
            log.error("Handle.failed run the.netty.delayed.queue.task:[{}] error", JSON.toJSONString(task), e);
            // TODO retry?
        }
    }

    /**
     * Handle
     *
     * @param task {@link Task<T>} task
     */
    protected abstract void handle(Task<T> task);

    public <HANDLER extends AbstractDelayedTaskHandler<T>> HANDLER data(T data) {
        this.data = data;

        return (HANDLER) this;
    }

    public <HANDLER extends AbstractDelayedTaskHandler<T>> HANDLER build() {
        Task<T> task = Task.<T>builder().id(this.taskId()).data(data).build();
        this.setTask(task);

        return (HANDLER) this;
    }

    public Task<T> task() {
        return task;
    }

    public T data() {
        return data;
    }

    private String taskId() {
        return this.generator.generateId().toString();
    }
}
