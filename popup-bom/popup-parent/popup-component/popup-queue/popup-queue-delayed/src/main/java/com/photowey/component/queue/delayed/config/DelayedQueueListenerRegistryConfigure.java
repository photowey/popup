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
package com.photowey.component.queue.delayed.config;

import com.photowey.component.queue.delayed.registry.DefaultDelayedQueueListenerRegistry;
import com.photowey.component.queue.delayed.registry.DelayedQueueListenerRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * {@code DelayedQueueListenerRegistryConfigure}
 *
 * @author photowey
 * @date 2023/01/18
 * @since 1.0.0
 */
@AutoConfiguration
public class DelayedQueueListenerRegistryConfigure {

    @Bean
    @ConditionalOnMissingBean
    public DelayedQueueListenerRegistry delayedQueueListenerRegistry() {
        return new DefaultDelayedQueueListenerRegistry();
    }

}
