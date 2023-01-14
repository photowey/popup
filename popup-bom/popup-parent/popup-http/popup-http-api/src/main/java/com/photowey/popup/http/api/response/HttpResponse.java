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
package com.photowey.popup.http.api.response;

import com.alibaba.fastjson2.JSON;
import com.photowey.component.common.util.ObjectUtils;
import com.photowey.component.common.util.StringFormatUtils;
import com.photowey.popup.http.api.exception.JsonSyntaxException;
import com.photowey.popup.http.api.model.header.HttpHeaders;
import com.photowey.popup.http.api.request.HttpRequest;
import com.photowey.popup.http.api.response.body.JsonResponseBody;
import com.photowey.popup.http.api.response.body.ResponseBody;

import java.io.Serializable;
import java.util.*;

/**
 * {@code HttpResponse}
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
public class HttpResponse<T> implements Serializable {

    private static final long serialVersionUID = 7330931757780889135L;

    private final HttpRequest request;
    private final HttpHeaders headers;
    private final ResponseBody body;
    private final List<T> collectionResponse = new ArrayList<>();

    public HttpResponse(HttpRequest request, HttpHeaders headers) {
        this(request, headers, null);
    }

    public HttpResponse(HttpRequest request, HttpHeaders headers, ResponseBody body) {
        this(request, headers, body, (T) null);
    }

    private HttpResponse(HttpRequest request, HttpHeaders headers, ResponseBody body, T response) {
        this(request, headers, body, List.of(response));
    }

    private HttpResponse(HttpRequest request, HttpHeaders headers, ResponseBody body, List<T> response) {
        this.request = request;
        this.headers = headers;
        this.body = body;
        if (ObjectUtils.isNotNullOrEmpty(response)) {
            this.collectionResponse.addAll(response);
        }
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public ResponseBody getBody() {
        return body;
    }

    public T getResponse() {
        return ObjectUtils.isNotNullOrEmpty(this.collectionResponse)
                ? this.collectionResponse.get(0)
                : null;
    }

    public List<T> getCollectionResponse() {
        return collectionResponse;
    }

    public Set<T> getCollectionResponseSet() {
        return new HashSet<>(collectionResponse);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<T> {
        private OriginalResponse originalResponse;
        private Class<T> responseType;
        private boolean collectionResponse;

        public Builder<T> originalResponse(OriginalResponse originalResponse) {
            this.originalResponse = originalResponse;
            return this;
        }

        public Builder<T> responseType(Class<T> responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder<T> collectionResponse(boolean collectionResponse) {
            this.collectionResponse = collectionResponse;
            return this;
        }

        public HttpResponse<T> build() {
            Objects.requireNonNull(originalResponse);
            if (ObjectUtils.isNullOrEmpty(originalResponse) || responseType == null) {
                return new HttpResponse<>(originalResponse.getRequest(), originalResponse.getHeaders(), null, null);
            }

            ResponseBody body = JsonResponseBody
                    .builder()
                    .body(originalResponse.getJsonBody())
                    .build();

            try {
                if (this.collectionResponse) {
                    List<T> response = JSON.parseArray(body.getContentType(), this.responseType);
                    return new HttpResponse<>(originalResponse.getRequest(), originalResponse.getHeaders(), body, response);
                }

                T response = JSON.parseObject(body.getContentType(), this.responseType);
                return new HttpResponse<>(originalResponse.getRequest(), originalResponse.getHeaders(), body, response);
            } catch (Exception e) {
                throw new JsonSyntaxException(StringFormatUtils.format("Invalid json response body:[{}]", originalResponse.getJsonBody()), e);
            }
        }
    }
}
