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
package com.photowey.popup.http.api.response;

import com.photowey.popup.http.api.model.header.HttpHeaders;
import com.photowey.popup.http.api.request.HttpRequest;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * {@code OriginalResponse}
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
public class OriginalResponse implements Serializable {

    private static final long serialVersionUID = -5975737371578982618L;

    private final HttpHeaders headers;
    private final HttpRequest request;
    private final String contentType;
    private final int statusCode;
    private final byte[] body;

    private OriginalResponse(HttpRequest request, int statusCode, HttpHeaders headers, String contentType, byte[] body) {
        this.request = request;
        this.contentType = contentType;
        this.headers = headers;
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public byte[] getBody() {
        return body;
    }

    public String getJsonBody() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public String getContentType() {
        return contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Serializable {

        private static final long serialVersionUID = -2741535751419395391L;

        private HttpRequest request;
        private int statusCode;
        private HttpHeaders headers;
        private String contentType;

        private byte[] body;

        public Builder request(HttpRequest request) {
            this.request = request;
            return this;
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = new HttpHeaders(headers);
            return this;
        }


        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public OriginalResponse build() {
            Objects.requireNonNull(request);
            return new OriginalResponse(request, statusCode, headers, contentType, body);
        }
    }
}
