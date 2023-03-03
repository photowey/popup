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
package com.photowey.popup.spring.cloud.gateway.app.config;

import com.photowey.popup.spring.cloud.gateway.app.filter.SwaggerGlobalFilter;
import com.photowey.popup.spring.cloud.gateway.app.filter.SwaggerHeaderFilter;
import com.photowey.popup.spring.cloud.gateway.app.swagger.SwaggerProvider;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/**
 * {@code SwaggerConfigure}
 *
 * @author photowey
 * @date 2023/02/28
 * @since 1.0.0
 */
@Configuration
public class SwaggerConfigure {

    @Bean
    public SwaggerGlobalFilter swaggerGlobalFilter() {
        return new SwaggerGlobalFilter();
    }

    @Bean
    public SwaggerHeaderFilter swaggerHeaderFilter() {
        return new SwaggerHeaderFilter();
    }

    @Bean
    @Primary
    public SwaggerProvider swaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        return new SwaggerProvider(routeLocator, gatewayProperties);
    }

    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder().build();
    }

    @Bean
    public UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder().showExtensions(true).build();
    }

}