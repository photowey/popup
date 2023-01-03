/*
 * Copyright © 2022 the original author or authors (photowey@gmail.com)
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
package com.photowey.popup.http.transport.config;

import com.photowey.popup.http.transport.okhttp.interceptor.DefaultRequestInterceptor;
import com.photowey.popup.http.transport.okhttp.interceptor.RequestInterceptor;
import com.photowey.popup.http.transport.property.PopupHttpProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@code PopupHttpAutoConfigure}
 *
 * @author photowey
 * @date 2023/01/03
 * @since 1.0.0
 */
@AutoConfiguration
@Import(value = {
        PopupHttpAutoConfigure.PropertyConfigure.class,
        PopupHttpAutoConfigure.BeanConfigure.class,
})
public class PopupHttpAutoConfigure {

    @Configuration
    @EnableConfigurationProperties(value = {
            PopupHttpProperties.class
    })
    static class PropertyConfigure {

    }

    @Configuration
    static class BeanConfigure {

        @Bean
        @ConditionalOnMissingBean
        public RequestInterceptor requestInterceptor() {
            return new DefaultRequestInterceptor();
        }
    }
}
