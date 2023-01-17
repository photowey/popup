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
package com.photowey.popup.http.api.client;

import com.photowey.popup.http.api.enums.HttpMethod;
import com.photowey.popup.http.api.model.header.HttpHeaders;
import com.photowey.popup.http.api.request.HttpRequest;
import com.photowey.popup.http.api.request.body.EmptyRequestBody;
import com.photowey.popup.http.api.request.body.RequestBody;
import com.photowey.popup.http.api.response.HttpResponse;

/**
 * {@code HttpClient}
 *
 * @author photowey
 * @date 2023/01/04
 * @since 1.0.0
 */
public interface HttpClient extends RequestExecutor, Downloader {

    default <T> HttpResponse<T> get(String url, Class<T> responseClass) {
        return this.get(null, url, responseClass);
    }

    default <T> HttpResponse<T> get(HttpHeaders headers, String url, Class<T> responseClass) {
        HttpRequest httpRequest = HttpRequest
                .builder()
                .url(url)
                .httpMethod(HttpMethod.GET)
                .headers(headers)
                .build();

        return this.execute(httpRequest, responseClass);
    }

    default <T> HttpResponse<T> post(String url, Class<T> responseClass) {
        return this.post(url, EmptyRequestBody.EMPTY, responseClass);
    }

    default <T> HttpResponse<T> post(String url, RequestBody body, Class<T> responseClass) {
        return this.post(HttpHeaders.empty(), url, body, responseClass);
    }

    default <T> HttpResponse<T> post(HttpHeaders headers, String url, RequestBody body, Class<T> responseClass) {
        HttpRequest httpRequest = HttpRequest
                .builder()
                .url(url)
                .httpMethod(HttpMethod.POST)
                .headers(headers)
                .body(body)
                .build();

        return this.execute(httpRequest, responseClass);
    }

    default <T> HttpResponse<T> put(String url, Class<T> responseClass) {
        return this.put(url, EmptyRequestBody.EMPTY, responseClass);
    }

    default <T> HttpResponse<T> put(String url, RequestBody body, Class<T> responseClass) {
        return this.put(HttpHeaders.empty(), url, body, responseClass);
    }

    default <T> HttpResponse<T> put(HttpHeaders headers, String url, RequestBody body, Class<T> responseClass) {
        HttpRequest httpRequest = HttpRequest
                .builder()
                .url(url)
                .httpMethod(HttpMethod.PUT)
                .headers(headers)
                .body(body)
                .build();

        return this.execute(httpRequest, responseClass);
    }

    default <T> HttpResponse<T> patch(String url, Class<T> responseClass) {
        return this.patch(url, EmptyRequestBody.EMPTY, responseClass);
    }

    default <T> HttpResponse<T> patch(String url, RequestBody body, Class<T> responseClass) {
        return this.patch(HttpHeaders.empty(), url, body, responseClass);
    }

    default <T> HttpResponse<T> patch(HttpHeaders headers, String url, RequestBody body, Class<T> responseClass) {
        HttpRequest httpRequest = HttpRequest
                .builder()
                .url(url)
                .httpMethod(HttpMethod.PATCH)
                .headers(headers)
                .body(body)
                .build();

        return this.execute(httpRequest, responseClass);
    }

    default <T> HttpResponse<T> delete(String url, Class<T> responseClass) {
        return this.delete(HttpHeaders.empty(), url, responseClass);
    }

    default <T> HttpResponse<T> delete(HttpHeaders headers, String url, Class<T> responseClass) {
        HttpRequest httpRequest = HttpRequest
                .builder()
                .url(url)
                .httpMethod(HttpMethod.DELETE)
                .headers(headers)
                .build();

        return this.execute(httpRequest, responseClass);
    }
}
