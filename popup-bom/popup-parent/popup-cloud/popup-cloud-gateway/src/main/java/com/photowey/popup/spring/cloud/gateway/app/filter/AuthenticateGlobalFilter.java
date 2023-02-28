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
package com.photowey.popup.spring.cloud.gateway.app.filter;

import com.photowey.component.exception.core.model.ExceptionModel;
import com.photowey.popup.spring.cloud.gateway.app.constant.GatewayConstants;
import com.photowey.popup.spring.cloud.gateway.app.engine.GatewayEngine;
import com.photowey.popup.spring.cloud.gateway.app.engine.GatewayEngineAware;
import com.photowey.popup.spring.cloud.gateway.app.printer.MonoPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

/**
 * {@code AuthenticateGlobalFilter}
 *
 * @author photowey
 * @date 2023/02/28
 * @since 1.0.0
 */
@Slf4j
public class AuthenticateGlobalFilter implements GlobalFilter, Ordered, GatewayEngineAware {

    private GatewayEngine gatewayEngine;

    @Override
    public void setGatewayEngine(GatewayEngine gatewayEngine) {
        this.gatewayEngine = gatewayEngine;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return GatewayConstants.ORDERED_OAUTH2_GLOBAL_FILTER;
    }

    private boolean shouldSkip(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String path = this.determineRequestPath(request);

        Set<String> ignorePaths = this.determineIgnorePaths();
        for (String ignorePath : ignorePaths) {
            PathPattern pattern = PathPatternParser.defaultInstance.parse(ignorePath);
            boolean matches = pattern.matches(PathContainer.parsePath(path));
            if (matches) {
                return true;
            }
        }

        return false;
    }

    private String determineRequestPath(ServerHttpRequest request) {
        String path = request.getPath().toString();

        return path;
    }

    private Set<String> determineIgnorePaths() {
        // TODO
        return new HashSet<>();
    }

    private boolean isBearerToken(String token) {
        return true;
    }

    private boolean isNotBearerToken(String token) {
        return false;
    }

    private boolean determineDeny(String bearerToken) {
        // TODO
        return false;
    }

    private String issueInnerToken(String bearerToken) {
        String tokenValue = this.resolveBearerToken(bearerToken);

        // TODO
        return tokenValue;
    }

    private String resolveBearerToken(String bearerToken) {
        return bearerToken;
    }

    public ExceptionModel deny() {
        return ExceptionModel.unauthorized();
    }

    public ExceptionModel deny(String message) {
        return ExceptionModel.unauthorized(message);
    }

    private <T> Mono<Void> toMono(T body, ServerWebExchange exchange) {
        return MonoPrinter.toMono(body, HttpStatus.UNAUTHORIZED, exchange);
    }

    @Deprecated
    private AntPathMatcher createMatcher(boolean caseSensitive) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setTrimTokens(false);
        matcher.setCaseSensitive(caseSensitive);

        return matcher;
    }
}
