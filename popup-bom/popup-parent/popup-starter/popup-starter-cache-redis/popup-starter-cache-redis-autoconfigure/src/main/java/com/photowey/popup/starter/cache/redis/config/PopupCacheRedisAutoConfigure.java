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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.photowey.popup.starter.cache.redis.core.constant.RedisConstants;
import com.photowey.popup.starter.cache.redis.engine.DefaultRedisEngine;
import com.photowey.popup.starter.cache.redis.engine.RedisEngine;
import com.photowey.popup.starter.cache.redis.property.RedisProperties;
import com.photowey.popup.starter.cache.redis.proxy.DefaultRedisTemplateProxy;
import com.photowey.popup.starter.cache.redis.template.RedisTemplateProxy;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * {@code PopupCacheRedisAutoConfigure}
 *
 * @author photowey
 * @date 2023/01/31
 * @since 1.0.0
 */
@ConditionalOnClass(RedissonClient.class)
@AutoConfiguration(after = RedisAutoConfiguration.class)
@EnableConfigurationProperties(value = {
        JedisPoolConfigure.class,
        JedisConfigure.class,
})
public class PopupCacheRedisAutoConfigure {

    private static final String CUSTOM_REDIS_TEMPLATE_BEAN_NAME = RedisConstants.CUSTOM_REDIS_TEMPLATE_BEAN_NAME;

    private static final String REDIS_KEY_SERIALIZER_BEAN_NAME = RedisConstants.REDIS_KEY_SERIALIZER_BEAN_NAME;
    private static final String REDIS_VALUE_SERIALIZER_BEAN_NAME = RedisConstants.REDIS_VALUE_SERIALIZER_BEAN_NAME;

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    @ConditionalOnMissingBean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(this.redisProperties.getHost());
        redisStandaloneConfiguration.setPort(this.redisProperties.getPort());
        redisStandaloneConfiguration.setDatabase(this.redisProperties.getDatabase());
        if (this.redisProperties.determinePasswordIsEnabled()) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(this.redisProperties.getPassword()));
        }

        return redisStandaloneConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisConnectionFactory connectionFactory(JedisPoolConfig jedisPoolConfig) {
        RedisStandaloneConfiguration std = this.redisStandaloneConfiguration();

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder configurationBuilder =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        configurationBuilder.poolConfig(jedisPoolConfig);
        JedisClientConfiguration conf = configurationBuilder.build();

        return new JedisConnectionFactory(std, conf);
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);

        return stringRedisTemplate;
    }

    @Bean(REDIS_KEY_SERIALIZER_BEAN_NAME)
    @ConditionalOnMissingBean(name = REDIS_KEY_SERIALIZER_BEAN_NAME)
    public RedisSerializer<String> redisKeySerializer() {
        return new StringRedisSerializer();
    }

    @Bean(REDIS_VALUE_SERIALIZER_BEAN_NAME)
    @ConditionalOnMissingBean(name = REDIS_VALUE_SERIALIZER_BEAN_NAME)
    @ConditionalOnExpression("#{T(org.springframework.util.StringUtils).hasText(environment['spring.redis.serializer']) " +
            "&& environment['spring.redis.serializer'].startsWith('Jackson')}")
    public RedisSerializer<Object> redisValueJacksonSerializer(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());

        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        return redisSerializer;
    }

    @Bean(REDIS_VALUE_SERIALIZER_BEAN_NAME)
    @ConditionalOnMissingBean(name = REDIS_VALUE_SERIALIZER_BEAN_NAME)
    @ConditionalOnExpression("#{T(org.springframework.util.StringUtils).hasText(environment['spring.redis.serializer']) " +
            "&& environment['spring.redis.serializer'].startsWith('Generic')}")
    public RedisSerializer<Object> redisValueGenericSerializer(ObjectMapper objectMapper) {
        //objectMapper.registerModule(new JavaTimeModule());

        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean(CUSTOM_REDIS_TEMPLATE_BEAN_NAME)
    @ConditionalOnMissingBean(name = CUSTOM_REDIS_TEMPLATE_BEAN_NAME)
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            @Qualifier(REDIS_KEY_SERIALIZER_BEAN_NAME) RedisSerializer<String> redisKeySerializer,
            @Qualifier(REDIS_VALUE_SERIALIZER_BEAN_NAME) RedisSerializer<Object> redisValueSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);

        redisTemplate.setValueSerializer(redisValueSerializer);
        redisTemplate.setHashValueSerializer(redisValueSerializer);

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(RedisTemplateProxy.class)
    public RedisTemplateProxy redisTemplateProxy(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        return new DefaultRedisTemplateProxy(redisTemplate, stringRedisTemplate);
    }

    // ----------------------------------------------------------------

    @Bean
    public RedisEngine redisEngine() {
        return new DefaultRedisEngine();
    }
}


