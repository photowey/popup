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
package com.photowey.popup.starter.autoconfigure.config;

import com.photowey.popup.spring.cloud.core.config.serializer.LocalDateTimeTimeStampFormatterConfigurer;
import com.photowey.popup.spring.cloud.core.config.trimmer.SpaceTrimmerConfigure;
import com.photowey.popup.spring.cloud.core.converter.json.DefaultJacksonJsonConverter;
import com.photowey.popup.spring.cloud.core.converter.json.JsonConverter;
import com.photowey.popup.spring.cloud.core.injector.ApplicationContextInjector;
import com.photowey.popup.starter.autoconfigure.cleaner.ResourceCleaner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@code PopupAutoConfigure}
 *
 * @author photowey
 * @date 2023/03/03
 * @since 1.0.0
 */
@AutoConfiguration
@Import(value = {
        LocalDateTimeTimeStampFormatterConfigurer.class,
        SpaceTrimmerConfigure.class,
        PopupAutoConfigure.InfrasConfigure.class,
})
public class PopupAutoConfigure {

    @Configuration
    class InfrasConfigure {

        @Bean
        public ResourceCleaner resourceCleaner() {
            return new ResourceCleaner();
        }

        @Bean
        public ApplicationContextInjector applicationContextInjector() {
            return new ApplicationContextInjector();
        }
    }

    @Bean
    @ConditionalOnMissingBean(JsonConverter.class)
    public JsonConverter json() {
        return new DefaultJacksonJsonConverter();
    }
}
