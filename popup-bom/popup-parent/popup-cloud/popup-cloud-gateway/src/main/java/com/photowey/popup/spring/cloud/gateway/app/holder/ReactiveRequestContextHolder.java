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
package com.photowey.popup.spring.cloud.gateway.app.holder;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * {@code ReactiveRequestContextHolder}
 *
 * @author photowey
 * @date 2023/02/26
 * @since 1.0.0
 */
public final class ReactiveRequestContextHolder {

    private ReactiveRequestContextHolder() {
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public static Mono<ServerHttpRequest> getRequest() {
        return Mono.deferContextual(Mono::just).map(ctx -> ctx.get(Holder.CTX).getRequest());
    }

    public static Mono<ServerHttpResponse> getResponse() {
        return Mono.deferContextual(Mono::just).map(ctx -> ctx.get(Holder.CTX).getResponse());
    }

    public static final class Holder {
        public static final Class<ServerWebExchange> CTX = ServerWebExchange.class;
    }
}
