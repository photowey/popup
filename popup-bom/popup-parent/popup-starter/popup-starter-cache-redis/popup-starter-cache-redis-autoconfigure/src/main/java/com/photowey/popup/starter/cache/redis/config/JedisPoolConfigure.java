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

import com.photowey.popup.starter.cache.redis.property.JedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * {@code JedisPoolConfigure}
 *
 * @author weichangjun
 * @date 2023/12/24
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(value = {JedisProperties.class})
public class JedisPoolConfigure {

    @Autowired
    private JedisProperties jedisProperties;

    @Bean
    @ConditionalOnMissingBean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedis = new JedisPoolConfig();
        jedis.setMaxIdle(this.jedisProperties.getMaxIdle());
        jedis.setMaxWait(Duration.ofMillis(this.jedisProperties.getMaxWait()));
        jedis.setMaxTotal(this.jedisProperties.getMaxTotal());
        jedis.setMinIdle(this.jedisProperties.getMinIdle());

        return jedis;
    }
}