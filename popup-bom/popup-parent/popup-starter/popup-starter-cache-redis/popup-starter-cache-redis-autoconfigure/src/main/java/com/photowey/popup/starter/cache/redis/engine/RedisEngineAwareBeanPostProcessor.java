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
package com.photowey.popup.starter.cache.redis.engine;

import com.photowey.popup.spring.cloud.core.engine.AbstractEngineAwareBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * {@code RedisEngineAwareBeanPostProcessor}
 *
 * @author photowey
 * @date 2024/01/19
 * @since 1.0.0
 */
public class RedisEngineAwareBeanPostProcessor extends AbstractEngineAwareBeanPostProcessor<RedisEngine, RedisEngineAware> {

    @Override
    public void inject(ConfigurableListableBeanFactory beanFactory, Object bean) {
        if (bean instanceof RedisEngineAware) {
            RedisEngine redisEngine = this.beanFactory.getBean(RedisEngine.class);
            ((RedisEngineAware) bean).setRedisEngine(redisEngine);
        }
    }
}
