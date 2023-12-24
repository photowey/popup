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

import com.photowey.popup.starter.cache.redis.property.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * {@code JedisConfigure}
 *
 * @author weichangjun
 * @date 2023/12/24
 * @since 1.0.0
 */
@EnableConfigurationProperties(value = {RedisProperties.class})
public class JedisConfigure {

    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Bean("redisPool")
    @ConditionalOnMissingBean
    @ConditionalOnExpression("#{T(org.springframework.util.StringUtils).hasText(environment['spring.redis.password'])==true}")
    public JedisPool passwordRedisPool() {
        JedisPoolConfig jedisPoolConfig = this.jedisPoolConfig;

        return new JedisPool(jedisPoolConfig,
                this.redisProperties.getHost(), this.redisProperties.getPort(), this.redisProperties.getTimeout(), this.redisProperties.getPassword());
    }

    @Bean("redisPool")
    @ConditionalOnMissingBean
    @ConditionalOnExpression("#{T(org.springframework.util.StringUtils).hasText(environment['spring.redis.password'])==false}")
    public JedisPool redisPool() {
        JedisPoolConfig jedisPoolConfig = this.jedisPoolConfig;

        return new JedisPool(jedisPoolConfig, this.redisProperties.getHost(), this.redisProperties.getPort(), this.redisProperties.getTimeout());
    }

}
