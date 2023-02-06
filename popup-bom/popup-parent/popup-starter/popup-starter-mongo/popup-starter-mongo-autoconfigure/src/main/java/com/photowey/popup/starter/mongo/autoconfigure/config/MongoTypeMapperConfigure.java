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
package com.photowey.popup.starter.mongo.autoconfigure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;

/**
 * {@code MongoTypeMapperConfigure}
 *
 * @author photowey
 * @date 2023/02/06
 * @since 1.0.0
 */
@Configuration
public class MongoTypeMapperConfigure {

    private static final String MONGO_TYPE_MAPPER_BEAN_NAME = "org.springframework.data.mongodb.core.convert.MongoTypeMapper";

    @Bean(MONGO_TYPE_MAPPER_BEAN_NAME)
    @ConditionalOnMissingBean(MongoTypeMapper.class)
    public MongoTypeMapper mongoTypeMapper() {
        return new DefaultMongoTypeMapper(null);
    }
}

