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
package com.photowey.popup.spring.cloud.gateway.app.swagger;

import com.photowey.component.common.formatter.StringFormatter;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@code SwaggerProvider}
 *
 * @author photowey
 * @date 2023/02/27
 * @since 1.0.0
 */
public class SwaggerProvider implements SwaggerResourcesProvider {

    public static final String API_VERSION = "v1.0.0";

    public static final String API_URI = "v3/api-docs";
    public static final String API_GROUP = "?group";
    public static final String API_GROUP_TEMPLATE = "{}?group={}";

    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;

    public SwaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        this.routeLocator = routeLocator;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        Set<String> routes = new HashSet<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        gatewayProperties.getRoutes().stream().filter(route -> routes.contains(route.getId())).forEach(route -> {
            route.getPredicates()
                    .stream()
                    .filter(def -> ("Path").equalsIgnoreCase(def.getName()))
                    .forEach(def -> resources.add(this.swaggerResource(route.getId(), this.toLocation(def))));
        });

        return resources;
    }

    @NotNull
    private String toLocation(PredicateDefinition def) {
        return def.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("**", API_URI);
    }

    private SwaggerResource swaggerResource(String serviceId, String location) {
        SwaggerResource resource = new SwaggerResource();
        resource.setName(serviceId);
        String url = this.withGroupIfNecessary(serviceId, location);
        resource.setLocation(url);
        resource.setUrl(url);
        resource.setSwaggerVersion(API_VERSION);

        return resource;
    }

    private String withGroupIfNecessary(String serviceId, String location) {
        if (location.contains(API_GROUP)) {
            return location;
        }

        return StringFormatter.format(API_GROUP_TEMPLATE, location, serviceId);
    }
}
