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
package com.photowey.popup.starter.cache.redis.config;

import com.photowey.component.sync.lock.Lock;
import com.photowey.popup.starter.cache.redis.lock.RedisLock;
import com.photowey.popup.starter.cache.redis.property.RedisLockProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * {@code CacheRedisAutoConfigure}
 *
 * @author photowey
 * @date 2023/01/31
 * @since 1.0.0
 */
@AutoConfiguration
@EnableConfigurationProperties(value = {RedisLockProperties.class})
public class CacheRedisAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(Lock.class)
    @ConditionalOnProperty(name = "spring.redis.lock.redisson.enabled", havingValue = "true", matchIfMissing = false)
    public Lock redisLock() {
        return new RedisLock();
    }
}
