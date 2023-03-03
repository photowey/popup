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
package com.photowey.popup.spring.cloud.gateway.app.printer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * {@code MonoPrinter}
 *
 * @author photowey
 * @date 2023/02/26
 * @since 1.0.0
 */
public final class MonoPrinter {

    private MonoPrinter() {
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public static <T> Mono<Void> toMono(T body, ServerWebExchange exchange) {
        return toMono(body, HttpStatus.OK, exchange);
    }

    public static <T> Mono<Void> toMono(T body, HttpStatus httpStatus, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        byte[] jsonBytes = JSON.toJSONBytes(body, JSONWriter.Feature.WriteMapNullValue);
        DataBuffer buffer = response.bufferFactory().wrap(jsonBytes);
        populateHeaders(response);

        return response.writeWith(Mono.just(buffer));
    }

    private static void populateHeaders(ServerHttpResponse response) {
        populateContentTypeIfNecessary(response);

        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.getHeaders().set(HttpHeaders.CACHE_CONTROL, "no-cache");
    }

    private static void populateContentTypeIfNecessary(ServerHttpResponse response) {
        if (notContainsContentType(response)) {
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }
    }

    private static boolean notContainsContentType(ServerHttpResponse response) {
        return !containsContentType(response);
    }

    private static boolean containsContentType(ServerHttpResponse response) {
        return response.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE);
    }
}
