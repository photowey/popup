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
import com.photowey.popup.starter.cache.redis.core.constant.RedisConstants;
import com.photowey.popup.starter.cache.redis.lock.RedisLock;
import com.photowey.popup.starter.cache.redis.lock.property.RedisLockProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * {@code PopupCacheRedisLockAutoConfigure}
 *
 * @author weichangjun
 * @date 2023/12/24
 * @since 1.0.0
 */
@ConditionalOnClass(RedissonClient.class)
@AutoConfiguration(after = RedisAutoConfiguration.class)
@EnableConfigurationProperties(value = {
        RedisLockProperties.class,
})
public class PopupCacheRedisLockAutoConfigure {

    private static final String REDIS_REGISTRY_KEY = RedisConstants.REDIS_REGISTRY_KEY;

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "spring.redis.lock.registry.enabled", havingValue = "true", matchIfMissing = false)
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, REDIS_REGISTRY_KEY);
    }

    @Bean
    @ConditionalOnMissingBean(Lock.class)
    @ConditionalOnProperty(name = "spring.redis.lock.redisson.enabled", havingValue = "true", matchIfMissing = false)
    public Lock redisLock(RedisLockProperties properties) {
        RedissonClient redisson = this.populateRedissonClient(properties);
        return new RedisLock(redisson, properties);
    }

    private RedissonClient populateRedissonClient(RedisLockProperties properties) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(properties.getAddress())
                .setDatabase(properties.getDatabase())
                .setPassword(properties.getPassword())
                .setTimeout(properties.getTimeout());

        return Redisson.create(config);
    }
}
