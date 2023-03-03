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
package com.photowey.popup.spring.cloud.gateway.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * {@code ExecutorConfigure}
 *
 * @author photowey
 * @date 2023/02/26
 * @since 1.0.0
 */
@Configuration
public class ExecutorConfigure {

    /**
     * @return {@link ThreadPoolTaskExecutor}
     */
    @Bean("commonAsyncExecutor")
    public ThreadPoolTaskExecutor commonAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1 << 1);
        taskExecutor.setMaxPoolSize(1 << 2);
        taskExecutor.setQueueCapacity(1 << 10);
        taskExecutor.setKeepAliveSeconds(1 << 6);
        taskExecutor.setThreadGroupName("async");
        taskExecutor.setThreadNamePrefix("gateway" + "-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setAllowCoreThreadTimeOut(true);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.initialize();

        return taskExecutor;
    }
}
