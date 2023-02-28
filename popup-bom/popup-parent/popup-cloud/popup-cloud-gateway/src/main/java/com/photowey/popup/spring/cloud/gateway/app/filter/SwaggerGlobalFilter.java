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

import com.jayway.jsonpath.JsonPath;
import com.photowey.component.common.constant.PopupConstants;
import com.photowey.popup.spring.cloud.gateway.app.constant.GatewayConstants;
import com.photowey.popup.spring.cloud.gateway.app.swagger.SwaggerProvider;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code SwaggerGlobalFilter}
 *
 * @author photowey
 * @date 2023/02/28
 * @since 1.0.0
 */
public class SwaggerGlobalFilter implements GlobalFilter, Ordered {

    private static final String API_DOC_URI = SwaggerProvider.API_URI;
    private static final String SWAGGER_API_DOC_HOST = "host";
    private static final String SWAGGER_API_DOC_BASE_PATH = "basePath";

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        String host = request.getLocalAddress().getHostString();
        int port = request.getLocalAddress().getPort();
        if (!path.endsWith(API_DOC_URI)) {
            return chain.filter(exchange);
        }

        String[] paths = path.split(PopupConstants.PATH_SEPARATOR);
        String basePath = paths[1];
        ServerHttpResponse originalResponse = exchange.getResponse();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (super.getStatusCode().equals(HttpStatus.OK) && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        List<String> buffers = new ArrayList<>();
                        dataBuffers.forEach(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);
                            buffers.add(new String(content, StandardCharsets.UTF_8));
                        });
                        String json = this.toJSONString(buffers);
                        json = JsonPath.parse(json)
                                .put("$", SWAGGER_API_DOC_HOST, host + PopupConstants.COLON_SEPARATOR + port)
                                .put("$", SWAGGER_API_DOC_BASE_PATH, PopupConstants.PATH_SEPARATOR + basePath)
                                .jsonString();

                        byte[] byteBody = json.getBytes(StandardCharsets.UTF_8);
                        int length = byteBody.length;
                        HttpHeaders headers = originalResponse.getHeaders();
                        headers.setContentLength(length);

                        return bufferFactory().wrap(byteBody);
                    }));
                }

                return super.writeWith(body);
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = super.getHeaders();
                httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                return httpHeaders;
            }

            private String toJSONString(List<String> buffers) {
                StringBuilder pool = new StringBuilder();
                for (String buf : buffers) {
                    pool.append(buf);
                }
                return pool.toString();
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return GatewayConstants.ORDERED_SWAGGER_GLOBAL_FILTER;
    }
}
