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

import com.photowey.popup.starter.mongo.autoconfigure.listener.AnnotationMongoOperationEventListener;
import com.photowey.popup.starter.mongo.autoconfigure.listener.MongoOperationEventListener;
import com.photowey.popup.starter.mongo.core.constant.MongoConstants;
import com.photowey.popup.starter.mongo.service.generator.MongoKeyGenerator;
import com.photowey.popup.starter.mongo.service.generator.snowflake.SnowflakeKeyGenerator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * {@code PopupMongoAutoConfigure}
 *
 * @author photowey
 * @date 2023/02/06
 * @since 1.0.0
 */
@AutoConfiguration(after = MongoAutoConfiguration.class)
@Import(value = {MongoTypeMapperConfigure.class})
public class PopupMongoAutoConfigure {

    private static final String MONGO_TRANSACTION_MANAGER_BEAN_NAME = MongoConstants.MONGO_TRANSACTION_MANAGER_BEAN_NAME;

    @Bean
    public MongoConverter mappingMongoConverter(
            MongoDatabaseFactory mongoDatabaseFactory,
            MongoTypeMapper mongoTypeMapper,
            MongoMappingContext context,
            MongoCustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter mmc = new MappingMongoConverter(dbRefResolver, context);
        mmc.setCustomConversions(conversions);

        // remove "_class" property
        mmc.setTypeMapper(mongoTypeMapper);

        return mmc;
    }

    @Bean
    public MongoOperationEventListener mongoOperationEventListener() {
        return new MongoOperationEventListener();
    }

    @Bean
    public AnnotationMongoOperationEventListener annotationMongoOperationEventListener() {
        return new AnnotationMongoOperationEventListener();
    }

    /**
     * single mode
     *
     * @return {@link MongoTransactionManager}
     */
    /*
    @Bean(MONGO_TRANSACTION_MANAGER_BEAN_NAME)
    @ConditionalOnMissingBean(MongoTransactionManager.class)
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory factory) {
        return new MongoTransactionManager(factory);
    }
    */
    @Bean
    @ConditionalOnMissingBean(MongoKeyGenerator.class)
    public MongoKeyGenerator mongoKeyGenerator() {
        return new SnowflakeKeyGenerator();
    }
}
